package com.ps.omarmattr.qalby.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.model.solahTime.SolahItem
import com.ps.omarmattr.qalby.other.BUNDLE_EXTRA
import com.ps.omarmattr.qalby.other.SOLAH_ITEM_EXTRA
import com.ps.omarmattr.qalby.ui.activity.MainActivity
import com.ps.omarmattr.qalby.util.PreferencesManager

private const val CHANNEL_ID = ""
private const val CHANNEL_NAME = "CHANNEL_NAME"

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("ooooooooooootttt", "onReceive")
        val bundle = intent!!.getBundleExtra(BUNDLE_EXTRA)
        bundle.getParcelable<SolahItem>(SOLAH_ITEM_EXTRA)?.let {
            Log.e("ooooooooooootttt", "getParcelableExtra $it")
            preferencesManager = PreferencesManager(context!!)
            if (preferencesManager.sharedPreferences.getBoolean(it.name, false)) {

                sendNotification(context, it)
            }
        }


    }

    private fun sendNotification(
        context: Context,
        solahItem: SolahItem
    ) {

        val title = solahItem.name + "موعد صلاه ال"
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // putExtra("product", product)
        }

        val pendingIntent =
            PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, 0)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dua)
            .setContentTitle(title)
            .setContentText("اشعار لموعد الصلاه")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("اشعار لموعد الصلاه")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true).setContentIntent(pendingIntent)

        preferencesManager.sharedPreferences.getInt(solahItem.name, 0).let {
            if (it != 0) builder.setSound(Uri.parse("android.resource://" + context.packageName + "/" + it))
        }

        createNotificationChannel(context)
        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }

    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}