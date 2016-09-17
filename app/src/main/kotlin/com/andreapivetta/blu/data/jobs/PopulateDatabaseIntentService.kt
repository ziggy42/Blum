package com.andreapivetta.blu.data.jobs

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.data.db.AppStorageFactory
import com.andreapivetta.blu.data.db.UserFollowed
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
        val storage = AppStorageFactory.getAppStorage(this)
        storage.clear()

        try {
            if (settings.isNotifyFavRet()) {
                Timber.i("Retrieving favorites/retweets")
                storage.saveTweetInfoList(NotificationsDataProvider.retrieveTweetInfo(twitter))
                AppSettingsImpl.setFavRetDownloaded(true)
            }

            if (settings.isNotifyMentions()) {
                Timber.i("Retrieving mentions")
                storage.saveMentions(NotificationsDataProvider.retrieveMentions(twitter))
                AppSettingsImpl.setMentionsDownloaded(true)
            }

            if (settings.isNotifyFollowers()) {
                Timber.i("Retrieving followers")
                storage.saveFollowers(NotificationsDataProvider.retrieveFollowers(twitter))
                AppSettingsImpl.setFollowersDownloaded(true)
            }

            if (settings.isNotifyDirectMessages()) {
                Timber.i("Retrieving private messages")
                storage.savePrivateMessages(NotificationsDataProvider
                        .retrievePrivateMessages(twitter))
                AppSettingsImpl.setDirectMessagesDownloaded(true)
            }

            Timber.i("Retrieving followed users")
            // If the user doesn't follow too many users (so we don't hit Twitter API limits),
            // retrieves all of them
            val users: List<UserFollowed> = NotificationsDataProvider
                    .safeRetrieveFollowedUsers(twitter)
            if (users.size > 0) {
                storage.saveUsersFollowed(users)
                AppSettingsImpl.setUserFollowedAvailable(true)
            }

            settings.setUserDataDownloaded(true)
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(Intent(BROADCAST_ACTION).putExtra(DATA_STATUS, true))
            NotificationsJob.scheduleJob()

            Timber.i("PopulateDatabaseIntentService finished.")
        } catch (err: Exception) {
            Timber.e(err, "Error running PopulateDatabaseIntentService")
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(Intent(BROADCAST_ACTION).putExtra(DATA_STATUS, false))
        }
    }

}
