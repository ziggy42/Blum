package com.andreapivetta.blu.data.jobs

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.data.db.AppStorageImpl
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

        try {
            Utils.runOnUiThread { AppStorageImpl.clear() }
            Utils.runOnUiThread { AppStorageImpl.init(this) }

            if (settings.isNotifyFavRet()) {
                Timber.i("Retrieving favorites/retweets")
                val infoList = NotificationsDataProvider.retrieveTweetInfo(twitter)
                Utils.runOnUiThread { AppStorageImpl.saveTweetInfoListA(infoList) }
                AppSettingsImpl.setFavRetDownloaded(true)
            }

            if (settings.isNotifyMentions()) {
                Timber.i("Retrieving mentions")
                val mentions = NotificationsDataProvider.retrieveMentions(twitter)
                Utils.runOnUiThread { AppStorageImpl.saveMentions(mentions) }
                AppSettingsImpl.setMentionsDownloaded(true)
            }

            if (settings.isNotifyFollowers()) {
                Timber.i("Retrieving followers")
                val followers = NotificationsDataProvider.retrieveFollowers(twitter)
                Utils.runOnUiThread { AppStorageImpl.saveFollowers(followers) }
                AppSettingsImpl.setFollowersDownloaded(true)
            }

            if (settings.isNotifyDirectMessages()) {
                Timber.i("Retrieving private messages")
                val directMessages = NotificationsDataProvider.retrievePrivateMessages(twitter)
                Utils.runOnUiThread { AppStorageImpl.savePrivateMessages(directMessages) }
                AppSettingsImpl.setDirectMessagesDownloaded(true)
            }

            Timber.i("Retrieving followed users")
            // If the user doesn't follow too many users (so we don't hit Twitter API limits),
            // retrieves all of them
            val users: List<UserFollowed> = NotificationsDataProvider
                    .safeRetrieveFollowedUsers(twitter)
            if (users.size > 0) {
                Utils.runOnUiThread { AppStorageImpl.saveUsersFollowed(users) }
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
