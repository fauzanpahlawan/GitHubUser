package com.example.githubuser.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.githubuser.R
import com.example.githubuser.ui.main.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ID_REPEATING = 881
        const val NOTIFY_ID = 882
        const val CHANNEL_ID = "com.example.githubuser.Channel_01"
        const val CHANNEL_NAME = "com.example.githubuser.service.AlarmManagerChannel"
        const val BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }


    override fun onReceive(context: Context, intent: Intent?) {
        val title = context.getString(R.string.notification_title)
        val message = context.getString(R.string.notification_message)
        val notifyId = NOTIFY_ID

        notifyId.showAlarmNotification(context, title, message)

        if (intent?.action == BOOT_COMPLETED) {
            setRepeatingAlarm(context)
        }
    }

    private fun Int.showAlarmNotification(
        context: Context,
        title: String,
        message: String?
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_web_github)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = CHANNEL_NAME

            builder.setChannelId(CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(this, notification)
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        val calendarTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
        }

        //check if time already passed, and set for tomorrow and onward
        val now = Calendar.getInstance().timeInMillis
        val diff = now - calendarTime.timeInMillis
        if (diff > 0) {
            calendarTime.set(Calendar.DATE, 1)
        }

        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_REPEATING,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendarTime.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_NO_CREATE)
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    fun isAlarmSet(context: Context): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            ID_REPEATING,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }
}