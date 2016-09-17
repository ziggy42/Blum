package com.andreapivetta.blu.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.IntentCompat
import android.support.v7.app.AlertDialog
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.BuildConfig
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.data.db.AppStorageFactory
import com.andreapivetta.blu.data.twitter.TwitterUtils
import com.andreapivetta.blu.ui.base.custom.ThemedActivity
import com.andreapivetta.blu.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : ThemedActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }

        class SettingsFragment() : PreferenceFragment() {

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                addPreferencesFromResource(R.xml.pref_general)

                findPreference("pref_key_licenses").setOnPreferenceClickListener {
                    val webView = WebView(activity)
                    webView.loadUrl(BuildConfig.LICENSES_URL)

                    AlertDialog.Builder(activity).setView(webView)
                            .setTitle(getString(R.string.licenses_pref_title))
                            .setPositiveButton(getString(R.string.ok), null)
                            .show()

                    true
                }

                findPreference("pref_key_about").setOnPreferenceClickListener {
                    val dialogView = View.inflate(activity, R.layout.dialog_about, null)
                    val mTwitterTextView = dialogView.findViewById(R.id.aboutTextView) as TextView
                    mTwitterTextView.text = Html.fromHtml(getString(R.string.my_twitter))
                    mTwitterTextView.movementMethod = LinkMovementMethod.getInstance()
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
            }

            private fun logout() {
                AppSettingsImpl.clear()
                AppStorageFactory.getAppStorage(activity).clear()
                TwitterUtils.nullTwitter()
                activity.finish()
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(intent)
            }

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
