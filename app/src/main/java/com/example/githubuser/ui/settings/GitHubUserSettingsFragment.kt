package com.example.githubuser.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.githubuser.R
import com.example.githubuser.ui.callback.NotificationSettingsChangeListener
import java.util.*

class GitHubUserSettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var notificationSettingsChangeListener: NotificationSettingsChangeListener? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is NotificationSettingsChangeListener) {
            notificationSettingsChangeListener =
                activity as NotificationSettingsChangeListener
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val languagePreference =
            findPreference<Preference>(requireContext().getString(R.string.key_language))

        val currentLanguage = Locale.getDefault().displayLanguage
        languagePreference?.summary =
            requireContext().getString(R.string.summary_language, currentLanguage)
        languagePreference?.setOnPreferenceClickListener {
            intentOpenLanguageSettings()
            true
        }
    }

    private fun intentOpenLanguageSettings() {
        val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(mIntent)
    }


    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            requireContext().getString(R.string.key_notification_pref) -> {
                val value = sharedPreferences?.getBoolean(key, false) as Boolean
                if (value) {
                    notificationSettingsChangeListener?.onNotifyTrue()
                } else {
                    notificationSettingsChangeListener?.onNotifyFalse()
                }
            }
        }
    }
}