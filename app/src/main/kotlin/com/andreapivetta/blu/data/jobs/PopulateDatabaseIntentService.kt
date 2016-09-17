package com.andreapivetta.blu.data.jobs

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.data.db.AppStorageImpl
import com.andreapivetta.blu.data.twitter.TwitterUtils
import timber.log.Timber

class PopulateDatabaseIntentService : IntentService("PopulateDatabaseIntentService") {

    companion object {
        val BROADCAST_ACTION = "com.andreapivetta.blu.data.jobs.BROADCAST"
        val DATA_STATUS = "PopulateDatabaseIntentService.STATUS"

        fun startService(context: Context) {
            context.startService(Intent(context, PopulateDatabaseIntentService::class.java))
        }
    }

    val settings = AppSettingsImpl

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null)
            throw RuntimeException("Intent cannot be null")

        Timber.i("Starting PopulateDatabaseIntentService...")

        val twitter = TwitterUtils.getTwitter()

        try {
            Utils.runOnUiThread { AppStorageImpl.clear() }
            Utils.runOnUiThread { AppStorageImpl.init(this) }

            if (settings.isNotifyFavRet()) {
                Timber.i("Retrieving favorites/retweets")
                PopulateDatabaseProvider.retrieveTweetInfo(twitter)
            }

            if (settings.isNotifyMentions()) {
                Timber.i("Retrieving mentions")
                PopulateDatabaseProvider.retrieveMentions(twitter)
            }

            if (settings.isNotifyFollowers()) {
                Timber.i("Retrieving followers")
                PopulateDatabaseProvider.retrieveFollowers(twitter)
            }

            if (settings.isNotifyDirectMessages()) {
                Timber.i("Retrieving private messages")
                PopulateDatabaseProvider.retrievePrivateMessages(twitter)
            }

            Timber.i("Retrieving followed users")
            // If the user doesn't follow too many users (so we don't hit Twitter API limits),
            // retrieves all of them
            PopulateDatabaseProvider.safeRetrieveFollowedUsers(twitter)

            settings.setUserDataDownloaded(true)
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(Intent(BROADCAST_ACTION).putExtra(DATA_STATUS, true))

            Timber.i("PopulateDatabaseIntentService finished.")
        } catch (err: Exception) {
            Timber.e(err, "Error running PopulateDatabaseIntentService")
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(Intent(BROADCAST_ACTION).putExtra(DATA_STATUS, false))
        }
    }

}
