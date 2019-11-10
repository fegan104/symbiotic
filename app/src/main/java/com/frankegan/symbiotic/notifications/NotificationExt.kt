package com.frankegan.symbiotic.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Fermentation
import com.frankegan.symbiotic.ui.MainActivity


/**
 * [NotificationManager] ID for displaying reminder notifications.
 */
const val REMINDER_NOTIF_ID = 101

const val CHANNEL_ID_REMINDER = "com.frankegan.symbiotic.reminders"

fun createReminderChannel(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val showBadge = true
    val name = context.getString(com.frankegan.symbiotic.R.string.channel_fermentation_reminder)
    val description = context.getString(com.frankegan.symbiotic.R.string.channel_desc_fermentation)

    val channel = NotificationChannel(CHANNEL_ID_REMINDER, name, NotificationManager.IMPORTANCE_DEFAULT)
    channel.description = description
    channel.setShowBadge(showBadge)

    val notificationManager = context.getSystemService<NotificationManager>()
    notificationManager?.createNotificationChannel(channel)
}

fun showReminderNotification(context: Context, fermentation: Fermentation, reminder: ReminderType) {

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 1, intent, 0)

    val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_REMINDER).apply {
        setSmallIcon(R.drawable.ic_bubble_black_24dp)
        setContentTitle("${reminder.label} fermentation for ${fermentation.title} is done.")
        priority = NotificationCompat.PRIORITY_DEFAULT
        setContentIntent(pendingIntent)
        setAutoCancel(true)
    }

    val notificationManager = context.getSystemService<NotificationManager>()
    notificationManager?.notify(REMINDER_NOTIF_ID, notificationBuilder.build())
}

/**
 * We show reminders for first and second fermentations.
 */
enum class ReminderType(val label: String) {
    First("First"),
    Second("Second")
}