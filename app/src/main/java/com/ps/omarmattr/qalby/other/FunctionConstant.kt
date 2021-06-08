package com.ps.omarmattr.qalby.other

import android.content.Context
import android.view.View
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.model.solahTime.Date
import com.ps.omarmattr.qalby.model.solahTime.SolahItem
import com.ps.omarmattr.qalby.model.solahTime.Timings
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object FunctionConstant{
     fun addSolah(context:Context,timings: Timings): ArrayList<SolahItem> {
        return arrayListOf(
            SolahItem(
                name = context.getString(R.string.fajr),
                time = time24to12(timings.fajr),
                state = false
            ),
            SolahItem(
                name = context.getString(R.string.sunless),
                time = time24to12(timings.sunrise),
                state = false
            ), SolahItem(
                name = context.getString(R.string.dhuhr),
                time = time24to12(timings.dhuhr),
                state = false
            ), SolahItem(
                name = context.getString(R.string.asr),
                time = time24to12(timings.asr),
                state = false
            ), SolahItem(
                name = context.getString(R.string.maghrib),
                time = time24to12(timings.maghrib),
                state = false
            ), SolahItem(
                name = context.getString(R.string.sunset),
                time = time24to12(timings.sunset),
                state = false
            ), SolahItem(
                name = context.getString(R.string.isha),
                time = time24to12(timings.isha),
                state = false
            )
        )
    }



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


