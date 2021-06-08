package com.ps.omarmattr.qalby.repository

import android.os.CountDownTimer
import android.util.Log
import com.ps.omarmattr.qalby.model.solahTime.SendParam
import com.ps.omarmattr.qalby.model.solahTime.SolahItem
import com.ps.omarmattr.qalby.network.SolahInterface
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
    : SolahInterface,
) {

    private val getSolahTimesLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))
    private val getSolahAddressLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))
    private val countDownLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))
    private lateinit var countDownTimer: CountDownTimer
    private val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())


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
        val df = SimpleDateFormat("hh:mm a", Locale.getDefault())
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
        solahItem.find { i -> sdf.parse(i.time)!!.after(date) }?.let {
            setCountDownTimer(it.name, it.time)
        }
    }

    private fun setCountDownTimer(name: String, time: String) {

        val start = currentDate()
        val end = sdf.parse(time)
        if (end != null) {
            var difference = end.time - start.time
            if (difference < 0) {
                val max = sdf.parse("24:00:00")
                val min = sdf.parse("00:00:00")
                if (max != null && min != null)
                    difference = (max.time - start.time) + (end.time - min.time)
            }
            CoroutineScope(Dispatchers.IO).launch {
                countDownLiveData.emit(ResultRequest.empty("$name $time"))}

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

    fun getPrayerTimesLiveData() = getSolahTimesLiveData
    fun getPrayerAddressLiveData() = getSolahAddressLiveData
    fun getCountDownTimerLiveData() = countDownLiveData
}