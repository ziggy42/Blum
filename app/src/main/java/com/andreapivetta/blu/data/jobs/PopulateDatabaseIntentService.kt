package com.andreapivetta.blu.data.jobs

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.data.twitter.Twitter
import timber.log.Timber

class PopulateDatabaseIntentService : IntentService("PopulateDatabaseIntentService") {

    companion object {
        const val BROADCAST_ACTION = "com.andreapivetta.blu.data.jobs.BROADCAST"
        const val DATA_STATUS = "PopulateDatabaseIntentService.STATUS"

        fun startService(context: Context) {
            context.startService(Intent(context, PopulateDatabaseIntentService::class.java))
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null)
            throw RuntimeException("Intent cannot be null")

        Timber.i("Starting PopulateDatabaseIntentService...")

        val twitter = Twitter.getInstance()
        val storage = AppStorageFactory.getAppStorage()
        val settings = AppSettingsFactory.getAppSettings(this)
        storage.clear()

        try {
            if (settings.isNotifyFavRet()) {
                Timber.i("Retrieving favorites/retweets")
                storage.saveTweetInfoList(NotificationsDataProvider.retrieveTweetInfo(twitter))
                settings.setFavRetDownloaded(true)
            }

            if (settings.isNotifyMentions()) {
                Timber.i("Retrieving mentions")
                storage.saveMentions(NotificationsDataProvider.retrieveMentions(twitter))
                settings.setMentionsDownloaded(true)
            }

            if (settings.isNotifyFollowers()) {
                Timber.i("Retrieving followers")
                storage.saveFollowers(NotificationsDataProvider.retrieveFollowers(twitter))
                settings.setFollowersDownloaded(true)
            }

            if (settings.isNotifyDirectMessages()) {
                Timber.i("Retrieving private messages")
                storage.savePrivateMessages(NotificationsDataProvider
                        .retrieveDirectMessages(twitter)
                        .map {
                            run {
                                val pm = PrivateMessage.valueOf(it, settings.getLoggedUserId())
                                pm.isRead = true
                                pm
                            }
                        })
                settings.setDirectMessagesDownloaded(true)
            }

            Timber.i("Retrieving followed users")
            // If the user doesn't follow too many users (so we don't hit Twitter API limits),
            // retrieves all of them
            settings.setUserFollowedAvailable(NotificationsDataProvider
                    .safeRetrieveFollowedUsers(twitter, { storage.saveUsersFollowed(it) }))

            settings.setUserDataDownloaded(true)
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(Intent(BROADCAST_ACTION).putExtra(DATA_STATUS, true))
            AppJobCreator.scheduleJobs()

            storage.close()
            Timber.i("PopulateDatabaseIntentService finished.")
        } catch (err: Exception) {
            Timber.e(err, "Error running PopulateDatabaseIntentService")
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(Intent(BROADCAST_ACTION).putExtra(DATA_STATUS, false))
        }
    }

}
