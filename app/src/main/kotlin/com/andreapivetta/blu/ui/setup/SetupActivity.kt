package com.andreapivetta.blu.ui.setup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.data.jobs.PopulateDatabaseIntentService
import kotlinx.android.synthetic.main.activity_setup.*
import timber.log.Timber

class SetupActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, SetupActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        setSupportActionBar(setupToolbar)

        LocalBroadcastManager.getInstance(this).registerReceiver(ResponseReceiver(),
                IntentFilter(PopulateDatabaseIntentService.BROADCAST_ACTION))

        startDownloadButton.setOnClickListener {
            AppSettingsImpl.setNotifyDirectMessages(directMessagesCheckBox.isChecked)
            AppSettingsImpl.setNotifyFavRet(favRetCheckBox.isChecked)
            AppSettingsImpl.setNotifyFollowers(followersCheckBox.isChecked)
            AppSettingsImpl.setNotifyMentions(mentionsCheckBox.isChecked)

            PopulateDatabaseIntentService.startService(this)

            startDownloadButton.visibility = View.GONE
            loadingViewGroup.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private inner class ResponseReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.i("Broadcast received")
            if (intent != null &&
                    intent.getBooleanExtra(PopulateDatabaseIntentService.DATA_STATUS, false)) {
                LocalBroadcastManager.getInstance(this@SetupActivity).unregisterReceiver(this)
                this@SetupActivity.finish()
            } else {
                PopulateDatabaseIntentService.startService(this@SetupActivity)
            }
        }
    }
}
