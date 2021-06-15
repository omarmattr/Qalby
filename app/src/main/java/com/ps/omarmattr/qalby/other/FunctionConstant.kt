package com.ps.omarmattr.qalby.other

import android.content.Context
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.model.Azan
import com.ps.omarmattr.qalby.model.solahTime.SolahItem
import com.ps.omarmattr.qalby.model.solahTime.Timings
import com.ps.omarmattr.qalby.util.PreferencesManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object FunctionConstant {
    fun addSolah(context: Context, timings: Timings): ArrayList<SolahItem> {
        val shared = PreferencesManager(context).sharedPreferences
        return arrayListOf(
            SolahItem(
                name = context.getString(R.string.fajr),
                time = time24to12(timings.fajr),
                state = shared.getBoolean(context.getString(R.string.fajr), false)
            ),
            SolahItem(
                name = context.getString(R.string.sunless),
                time = time24to12(timings.sunrise),
                state = shared.getBoolean(context.getString(R.string.sunless), false)
            ), SolahItem(
                name = context.getString(R.string.dhuhr),
                time = time24to12(timings.dhuhr),
                state = shared.getBoolean(context.getString(R.string.dhuhr), false)
            ), SolahItem(
                name = context.getString(R.string.asr),
                time = time24to12(timings.asr),
                state = shared.getBoolean(context.getString(R.string.asr), false)
            ), SolahItem(
                name = context.getString(R.string.maghrib),
                time = time24to12(timings.maghrib),
                state = shared.getBoolean(context.getString(R.string.maghrib), false)
            ), SolahItem(
                name = context.getString(R.string.sunset),
                time = time24to12(timings.sunset),
                state = shared.getBoolean(context.getString(R.string.sunset), false)
            ), SolahItem(
                name = context.getString(R.string.isha),
                time = time24to12(timings.isha),
                state = shared.getBoolean(context.getString(R.string.isha), false)
            )
        )
    }

    fun getAzanList() = arrayListOf(
        Azan(id = 1, media = R.raw.faizallong, image = R.drawable.ic_dua, name = "Faizal Tahir"),
        Azan(id = 2, media = R.raw.mizilong, image = R.drawable.ic_dua, name = "Mizi Wahid"),
        Azan(
            id = 3,
            media = R.raw.daoodsubuhlong,
            image = R.drawable.ic_dua,
            name = "Sheikh Daoood Butt"
        ),
        Azan(
            id = 4,
            media = R.raw.abdelkarimsubuhlong,
            image = R.drawable.ic_dua,
            name = "Abdelkarim Meswhwari"
        ),
        Azan(
            id = 5,
            media = R.raw.ustazzakariazayraksubuhlong,
            image = R.drawable.ic_dua,
            name = "Zakaria Zayrak"
        )
    )


    fun time24to12(time: String): String {
        return try {
            val sdf = SimpleDateFormat("H:mm", Locale.getDefault())
            val dateObj = sdf.parse(time)
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            time
        }
    }
}


