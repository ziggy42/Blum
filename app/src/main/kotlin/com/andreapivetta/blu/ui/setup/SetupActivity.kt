package com.andreapivetta.blu.ui.setup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.common.utils.show
import com.andreapivetta.blu.data.jobs.PopulateDatabaseIntentService
import kotlinx.android.synthetic.main.activity_setup.*
import timber.log.Timber

class SetupActivity : AppCompatActivity() {

    companion object {
        private val ARG_DOWNLOAD = "download"

        fun launch(context: Context) {
            context.startActivity(Intent(context, SetupActivity::class.java))
        }
    }

    private var downloadStarted = false
    private val responseReceiver = ResponseReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        setSupportActionBar(setupToolbar)

        if (savedInstanceState != null && savedInstanceState.getBoolean(ARG_DOWNLOAD, false))
            updateViewForDownload()

        LocalBroadcastManager.getInstance(this).registerReceiver(responseReceiver,
                IntentFilter(PopulateDatabaseIntentService.BROADCAST_ACTION))

        val settings = AppSettingsFactory.getAppSettings(this)
        startDownloadButton.setOnClickListener {
            settings.setNotifyDirectMessages(directMessagesCheckBox.isChecked)
            settings.setNotifyFavRet(favRetCheckBox.isChecked)
            settings.setNotifyFollowers(followersCheckBox.isChecked)
            settings.setNotifyMentions(mentionsCheckBox.isChecked)

            PopulateDatabaseIntentService.startService(this)
            updateViewForDownload()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(ARG_DOWNLOAD, downloadStarted)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this@SetupActivity).unregisterReceiver(responseReceiver)
    }

    private fun updateViewForDownload() {
        downloadStarted = true
        setupViewGroup.show(false)
        loadingViewGroup.show()
    }

    private inner class ResponseReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.i("Broadcast received")
            if (intent != null &&
                    intent.getBooleanExtra(PopulateDatabaseIntentService.DATA_STATUS, false)) {
                this@SetupActivity.finish()
            } else {
                PopulateDatabaseIntentService.startService(this@SetupActivity)
            }
        }
    }
}
