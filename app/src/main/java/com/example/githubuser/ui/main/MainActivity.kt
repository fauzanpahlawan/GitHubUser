package com.example.githubuser.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.example.githubuser.R
import com.example.githubuser.service.AlarmReceiver
import com.example.githubuser.ui.callback.NotificationSettingsChangeListener

class MainActivity : AppCompatActivity(), NotificationSettingsChangeListener {

    private val navController: NavController by lazy {
        findNavController(R.id.nav_controller)
    }

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(navController)

        alarmReceiver = AlarmReceiver()

        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(this)
        val isNotifyEnabled =
            sharedPreference.getBoolean(getString(R.string.key_notification_pref), false)

        val checkAlarm = alarmReceiver.isAlarmSet(this)
        if (checkAlarm) {
            alarmReceiver.cancelAlarm(this)
        } else {
            if (isNotifyEnabled) {
                alarmReceiver.setRepeatingAlarm(this)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNotifyTrue() {
        alarmReceiver.setRepeatingAlarm(this)
    }

    override fun onNotifyFalse() {
        alarmReceiver.cancelAlarm(this)
    }
}