package com.andreapivetta.blu.data.jobs

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.support.v4.content.LocalBroadcastManager
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.data.db.*
import com.andreapivetta.blu.data.twitter.TwitterUtils
import timber.log.Timber
import twitter4j.PagableResponseList
import twitter4j.Paging
import twitter4j.User

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
        val storage = AppStorageImpl

        try {
            runOnUiThread { storage.clear() }
            runOnUiThread { storage.init(this) }

            if (settings.isNotifyFavRet()) {
                Timber.i("Retrieving favorites/retweets")
                // Retrieving favorites/retweets
                twitter.getUserTimeline(Paging(1, 200)).forEach { tmp ->
                    try {
                        val favs = Utils.getFavoriters(tmp.id)
                        val retws = Utils.getRetweeters(tmp.id)
                        runOnUiThread { storage.saveTweetInfo(TweetInfo(tmp.id, favs, retws)) }
                    } catch (e: KotlinNullPointerException) {
                        Timber.e(e, "Error getting tweet info for tweet ${tmp.id}")
                        onFailure()
                    }
                }
            }

            if (settings.isNotifyMentions()) {
                Timber.i("Retrieving mentions")
                // Retrieving mentions
                val mentions = twitter.getMentionsTimeline(Paging(1, 200))
                        .map { x -> Mention.valueOf(x) }
                runOnUiThread { storage.saveMentions(mentions) }
            }

            if (settings.isNotifyFollowers()) {
                Timber.i("Retrieving followers")
                // Retrieving followers
                val ids = twitter.getFollowersIDs(-1)
                do {
                    val followers = ids.iDs.map { x -> Follower(x) }
                    runOnUiThread { storage.saveFollowers(followers) }
                } while (ids.hasNext())
            }

            if (settings.isNotifyDirectMessages()) {
                Timber.i("Retrieving private messages")
                // Retrieving private messages
                val directMessages: MutableList<PrivateMessage> = twitter.getDirectMessages(Paging(1, 200))
                        .map { x -> PrivateMessage.valueOf(x) }
                        .toMutableList()
                directMessages.addAll(twitter.getSentDirectMessages(Paging(1, 200))
                        .map { x -> PrivateMessage.valueOf(x) })
                runOnUiThread { storage.savePrivateMessages(directMessages) }
            }

            Timber.i("Retrieving followed users")
            // If the user doesn't follow too many users (so we don't hit Twitter API limits),
            // retrieves all of them
            if (twitter.showUser(twitter.id).friendsCount < 2800) {
                var cursor: Long = -1
                var pagableFollowings: PagableResponseList<User>
                do {
                    pagableFollowings = twitter.getFriendsList(twitter.id, cursor, 200)

                    val users = pagableFollowings.map { x -> UserFollowed.valueOf(x) }
                    runOnUiThread { storage.saveUsersFollowed(users) }
                    cursor = pagableFollowings.nextCursor
                } while (cursor != 0L)

                settings.setUserFollowedAvailable(true)
            }

            onSuccess()

            Timber.i("PopulateDatabaseIntentService finished.")
        } catch (err: Exception) {
            Timber.e(err, "Error running PopulateDatabaseIntentService")
            onFailure()
        }
    }

    private fun onSuccess() {
        settings.setUserDataDownloaded(true)
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(Intent(BROADCAST_ACTION).putExtra(DATA_STATUS, true))
    }

    private fun onFailure() {
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(Intent(BROADCAST_ACTION).putExtra(DATA_STATUS, false))
    }

    private fun runOnUiThread(body: () -> Unit) {
        Handler(Looper.getMainLooper()).post { body.invoke() }
    }

}
