package com.ps.omarmattr.qalby.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import com.ps.omarmattr.qalby.model.solahTime.Gregorian
import com.ps.omarmattr.qalby.model.solahTime.SendParam
import com.ps.omarmattr.qalby.model.solahTime.SolahItem
import com.ps.omarmattr.qalby.network.SolahInterface
import com.ps.omarmattr.qalby.other.BUNDLE_EXTRA
import com.ps.omarmattr.qalby.other.SOLAH_ITEM_EXTRA
import com.ps.omarmattr.qalby.receiver.AlarmReceiver
import com.ps.omarmattr.qalby.util.ResultRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolahRepository @Inject constructor(
    val solahInterface
    : SolahInterface
) {

    private val getSolahTimesLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))
    private val getSolahAddressLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))
    private val countDownLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))
    private lateinit var countDownTimer: CountDownTimer
    private val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
    private val sdf2 = SimpleDateFormat("hh:mm a", Locale.getDefault())


    fun getPrayerTimes(sendParam: SendParam) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = solahInterface.getPrayerTimes(
                latitude = sendParam.latitude,
                longitude = sendParam.longitude,
                method = sendParam.method,
                month = sendParam.month,
                year = sendParam.year
            )
            try {
                if (response.isSuccessful) {
                    try {
                        Log.e(this.javaClass.name, "getPrayerTimes")

                        response.body()?.let {
                            getSolahTimesLiveData.emit(ResultRequest.success(it))
                        }
                    } catch (e: Exception) {
                        ResultRequest.error(
                            "Ooops1: ${e.message}",
                            e
                        )
                    }
                } else {
                    getSolahTimesLiveData.emit(
                        ResultRequest.error(
                            "Ooops2: ${response.errorBody()!!.charStream().readText()}", ""
                        )
                    )
                }
            } catch (e: HttpException) {
                getSolahTimesLiveData.emit(
                    ResultRequest.error(
                        "Ooops3: ${e.message()}",
                        e
                    )
                )

            } catch (t: Throwable) {
                getSolahTimesLiveData.emit(
                    ResultRequest.error(
                        "Ooops: ${t.message}",
                        t
                    )
                )
            }
        }
    }


    fun getPrayerAddress(sendParam: SendParam, address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = solahInterface.getPrayerAddress(
                address = address,
                method = sendParam.method,
                month = sendParam.month,
                year = sendParam.year
            )
            try {
                if (response.isSuccessful) {
                    try {
                        response.body()?.let {
                            getSolahAddressLiveData.emit(ResultRequest.success(it))
                        }
                    } catch (e: Exception) {

                    }
                } else {
                    getSolahAddressLiveData.emit(
                        ResultRequest.error(
                            "Ooops: ${response.errorBody()!!.charStream().readText()}", ""
                        )
                    )
                }
            } catch (e: HttpException) {
                getSolahAddressLiveData.emit(
                    ResultRequest.error(
                        "Ooops: ${e.message()}",
                        e
                    )
                )

            } catch (t: Throwable) {
                getSolahAddressLiveData.emit(
                    ResultRequest.error(
                        "Ooops: ${t.message}",
                        t
                    )
                )
            }
        }
    }

    private fun currentDate(): Date {
        val df = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return sdf.parse(df.format(Date()))!!
    }

    private fun formatMilliSecondsToTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60) % 24).toInt()
        return "${twoDigitString(hours)}:${twoDigitString(minutes)}:${twoDigitString(seconds)}"
    }

    private fun twoDigitString(number: Int): String {
        if (number == 0) {
            return "00"
        }
        return if (number / 10 == 0) {
            "0$number"
        } else number.toString()
    }

    fun getNextTime(solahItem: ArrayList<SolahItem>) {
        val date = currentDate()
        solahItem.find { i -> sdf2.parse(i.time)!!.after(date) }?.let {
            setCountDownTimer(it.name, it.time)
        }
    }

    private fun setCountDownTimer(name: String, time: String) {

        val start = currentDate()
        val end = sdf2.parse(time)
        if (end != null) {
            var difference = end.time - start.time
            if (difference < 0) {
                val max = sdf.parse("24:00:00")
                val min = sdf.parse("00:00:00")
                if (max != null && min != null)
                    difference = (max.time - start.time) + (end.time - min.time)
            }
            CoroutineScope(Dispatchers.IO).launch {
                countDownLiveData.emit(ResultRequest.empty("$name $time"))
            }

            CoroutineScope(Dispatchers.Main).launch {
                if (::countDownTimer.isInitialized) countDownTimer.cancel()

                countDownTimer = object : CountDownTimer(difference, 1000) {
                    override fun onTick(times: Long) {
                        val value = formatMilliSecondsToTime(times)
                        CoroutineScope(Dispatchers.IO).launch {
                            countDownLiveData.emit(ResultRequest.success(value))

                        }

                    }

                    override fun onFinish() {
                        CoroutineScope(Dispatchers.IO).launch {
                            countDownLiveData.emit(ResultRequest.success("Adzan $name"))
                        }
                    }
                }

                countDownTimer.start()
            }
        }

    }

    fun alarmManager(context: Context, gregorian: Gregorian, time: String, solahItem: SolahItem) {
        val receiver = Intent(context, AlarmReceiver::class.java)
        val bundle = Bundle()
        bundle.putParcelable(SOLAH_ITEM_EXTRA, solahItem)
        receiver.putExtra(BUNDLE_EXTRA, bundle)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            solahItem.time.codePointAt(1),
            receiver,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, getFullTime(gregorian, time), pendingIntent)
    }

    private val newSdf = SimpleDateFormat("H:mm", Locale.getDefault())
    private val cal = Calendar.getInstance()
    private fun getHour(time: String): Int {
        cal.time = newSdf.parse(time)!!
        return cal.get(Calendar.HOUR_OF_DAY)
    }

    private fun getMinute(time: String): Int {
        cal.time = newSdf.parse(time)!!
        return cal.get(Calendar.MINUTE)
    }

    private fun getFullTime(gregorian: Gregorian, time: String): Long {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal[Calendar.YEAR] = gregorian.year.toInt()
        cal[Calendar.MONTH] = gregorian.month.number - 1
        cal[Calendar.DAY_OF_MONTH] = gregorian.day.toInt()
        cal[Calendar.HOUR_OF_DAY] = getHour(time)
        cal[Calendar.MINUTE] = getMinute(time)
//        Log.e(
//            "pppppp",
//            SimpleDateFormat("yyyy-MM_dd hh:mm a", Locale.getDefault()).format(cal.time)
//        )
        return cal.timeInMillis
    }

    fun getPrayerTimesLiveData() = getSolahTimesLiveData
    fun getPrayerAddressLiveData() = getSolahAddressLiveData
    fun getCountDownTimerLiveData() = countDownLiveData
}