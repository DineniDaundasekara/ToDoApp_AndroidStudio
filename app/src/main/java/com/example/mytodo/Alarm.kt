package com.example.mytodo

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mytodo.databinding.ActivityAlarmBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class Alarm : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding
    private lateinit var timePicker: MaterialTimePicker
    private lateinit var calendar: java.util.Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private val POST_NOTIFICATIONS_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //EdgeToEdge.enable(this)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_alarm)

        // Inflate the layout
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create notification channel
        createNotificationChannel()

        // Handle runtime permissions for notifications (Android 13+)
        requestNotificationPermission()

        // Select time for the alarm
        binding.selectTime.setOnClickListener {


            openTimePicker()
        }

        // Set the alarm
        binding.setAlarm.setOnClickListener {
            Log.d("AlarmActivity", "Set Alarm button clicked")

            setAlarm()
        }

        // Cancel the alarm
        binding.cancelAlarm.setOnClickListener {
            Log.d("AlarmActivity", "Set Alarm button clicked")

            cancelAlarm()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun openTimePicker() {
        Log.d("AlarmActivity", "Opening time picker")
        timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        try {
            timePicker.show(supportFragmentManager, "androidknowledge")
        } catch (e: Exception) {
            Log.e("AlarmActivity", "Error showing time picker", e)
        }

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val timeText = when {
                hour == 0 -> String.format("%02d:%02d AM", 12, minute)
                hour == 12 -> String.format("%02d:%02d PM", 12, minute)
                hour > 12 -> String.format("%02d:%02d PM", hour - 12, minute)
                else -> String.format("%02d:%02d AM", hour, minute)
            }

            binding.selectTime.text = timeText

            calendar = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, hour)
                set(java.util.Calendar.MINUTE, minute)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
        }
    }



    private fun setAlarm() {
        if (!::calendar.isInitialized) {
            Toast.makeText(this, "Please select a time first", Toast.LENGTH_SHORT).show()
            return
        }

        // Ensure the selected time is in the future
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Use setExactAndAllowWhileIdle for precise timing
        // Set the alarm to repeat daily
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show()
    }

    private fun cancelAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (!::alarmManager.isInitialized) {
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "akchannel"
            val descriptionText = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("androidknowledge", name, importance).apply {
                description = descriptionText
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    POST_NOTIFICATIONS_REQUEST_CODE
                )
            }
        }
    }
}