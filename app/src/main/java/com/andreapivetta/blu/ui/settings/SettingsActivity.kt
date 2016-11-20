package com.andreapivetta.blu.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.PreferenceFragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.IntentCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.andreapivetta.blu.BuildConfig
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.common.utils.openUrl
import com.andreapivetta.blu.common.utils.shareText
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.data.twitter.Twitter
import com.andreapivetta.blu.ui.custom.Theme
import com.andreapivetta.blu.ui.custom.ThemedActivity
import com.andreapivetta.blu.ui.login.LoginActivity
import com.andreapivetta.blu.ui.main.MainActivity
import com.andreapivetta.blu.ui.profile.UserActivity
import com.luseen.autolinklibrary.AutoLinkMode
import com.luseen.autolinklibrary.AutoLinkTextView
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : ThemedActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }

    class SettingsFragment() : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)

            findPreference("themes").onPreferenceChangeListener = OnPreferenceChangeListener {
                preference, newValue ->
                restartApplication()
                true
            }

            findPreference("pref_key_licenses").setOnPreferenceClickListener {
                openUrl(activity, BuildConfig.LICENSES_URL)
                true
            }

            findPreference("pref_key_about").setOnPreferenceClickListener {
                val dialogView = View.inflate(activity, R.layout.dialog_about, null)
                val aboutTextView = dialogView.findViewById(R.id.aboutTextView) as AutoLinkTextView
                aboutTextView.addAutoLinkMode(AutoLinkMode.MODE_MENTION)
                aboutTextView.setMentionModeColor(Theme.getColorPrimary(activity))
                aboutTextView.setAutoLinkOnClickListener { mode, text ->
                    if (mode === AutoLinkMode.MODE_MENTION) UserActivity.launch(activity, text)
                }
                aboutTextView.setAutoLinkText(getString(R.string.my_twitter))
                AlertDialog.Builder(activity).setView(dialogView).show()

                true
            }

            findPreference("pref_key_logout").setOnPreferenceClickListener {
                val image = ImageView(activity)
                image.setImageDrawable(
                        ContextCompat.getDrawable(activity, R.drawable.ic_mood_bad))

                AlertDialog.Builder(activity)
                        .setView(image)
                        .setTitle(getString(R.string.logout_title_dialog))
                        .setPositiveButton(getString(R.string.yes),
                                { dialogInterface, i -> logout() })
                        .setNegativeButton(getString(R.string.cancel), null).show()

                true
            }

            findPreference("pref_key_share").setOnPreferenceClickListener {
                shareText(activity, activity.getString(R.string.share_app_text))
                true
            }

            findPreference("pref_key_rate").setOnPreferenceClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri
                            .parse("market://details?id=${activity.applicationContext.packageName}")))
                } catch (err: Exception) {
                    Toast.makeText(activity, getString(R.string.missing_play_store), Toast.LENGTH_SHORT).show()
                }
                true
            }
        }

        private fun logout() {
            AppSettingsFactory.getAppSettings(activity).clear()
            AppStorageFactory.getAppStorage().clear()
            Twitter.nullTwitter()
            activity.finish()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
        }

        private fun restartApplication() {
            activity.finish()
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null)
            fragmentManager.beginTransaction()
                    .add(R.id.container_frameLayout, SettingsFragment()).commit()
    }

}
