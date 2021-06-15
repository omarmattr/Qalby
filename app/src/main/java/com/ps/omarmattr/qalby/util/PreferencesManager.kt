package com.ps.omarmattr.qalby.util

import android.content.Context
import android.content.SharedPreferences
import com.ps.omarmattr.qalby.other.PREFERENCES_NAME


class PreferencesManager(mContext: Context) {
    val sharedPreferences: SharedPreferences =
        mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    @Volatile
    private var instance: PreferencesManager? = null
    private val LOCK = Any()

    operator fun invoke(mContext: Context) =
        instance ?: synchronized(LOCK) {
            instance ?: PreferencesManager(mContext).also {
                instance = it
            }
        }

    private fun createPreferencesManager(mContext: Context) = PreferencesManager(mContext)

}