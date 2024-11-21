package com.example.mytodo

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {
@SuppressLint("UnsafeProtectedBroadcastReceiver")
override fun onReceive(context: Context?, intent: Intent?) {
    Log.d("AlarmReceiver", "Alarm triggered")
    // Check for notification permission (Android 13+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (context?.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Do not proceed if permission is not granted
            return
        }
    }

    val nextActivity = Intent(context, AlarmDestination::class.java)
    nextActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    // PendingIntent for launching NotificationActivity
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        nextActivity,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Build the notification
    val builder = NotificationCompat.Builder(context!!, "androidknowledge")
        .setSmallIcon(R.drawable.baseline_circle_notifications_24)
        .setContentTitle("Reminder")
        .setContentText("It's time to wake up")
        .setAutoCancel(true)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)

    // Issue the notification using NotificationManagerCompat
    val notificationManagerCompat = NotificationManagerCompat.from(context)
    notificationManagerCompat.notify(123, builder.build())
}
}
