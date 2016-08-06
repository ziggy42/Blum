package com.andreapivetta.blu.data.jobs

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.data.db.*
import com.andreapivetta.blu.data.twitter.TwitterUtils
import timber.log.Timber
import twitter4j.PagableResponseList
import twitter4j.Paging
import twitter4j.User

class PopulateRealmIntentService : IntentService("PopulateRealmIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null)
            throw RuntimeException("Intent cannot be null")

        Timber.i("Starting PopulateRealmIntentService...")

        val twitter = TwitterUtils.getTwitter()
        val storage = AppStorageImpl
        val settings = AppSettingsImpl

        try {
            // Retrieving favorites/retweets
            twitter.getUserTimeline(Paging(1, 200)).forEach { tmp ->
                try {
                    val favs = Utils.getFavoriters(tmp.id)
                    val retws = Utils.getRetweeters(tmp.id)
                    runOnUiThread { storage.saveTweetInfo(TweetInfo(tmp.id, favs, retws)) }
                } catch (e: KotlinNullPointerException) {
                    Timber.e(e, "Error getting tweet info for tweet ${tmp.id}")
                }
            }

            // Retrieving mentions
            twitter.getMentionsTimeline(Paging(1, 200)).
                    forEach { x -> runOnUiThread { storage.saveMention(Mention.valueOf(x)) } }

            // Retrieving followers
            val ids = twitter.getFollowersIDs(-1)
            do {
                ids.iDs.forEach { x -> runOnUiThread { storage.saveFollower(Follower(x)) } }
            } while (ids.hasNext())

            // Retrieving private messages
            twitter.getDirectMessages(Paging(1, 200)).
                    forEach { x ->
                        runOnUiThread {
                            storage.savePrivateMessage(PrivateMessage.valueOf(x))
                        }
                    }
            twitter.getSentDirectMessages(Paging(1, 200)).
                    forEach { x ->
                        runOnUiThread {
                            storage.savePrivateMessage(PrivateMessage.valueOf(x))
                        }
                    }

            settings.setRealmPopulated(true)

            // If the user doesn't follow too many users (so we don't hit Twitter API limits),
            // retrieves all of them
            if (twitter.showUser(twitter.id).friendsCount < 2800) {
                var cursor: Long = -1
                var pagableFollowings: PagableResponseList<User>
                do {
                    pagableFollowings = twitter.getFriendsList(twitter.id, cursor, 200)
                    pagableFollowings.
                            forEach { x ->
                                runOnUiThread {
                                    storage.saveUserFollowed(UserFollowed.valueOf(x))
                                }
                            }
                    cursor = pagableFollowings.nextCursor
                } while (cursor != 0L)

                settings.setUserFollowedAvailable(true)
            }

            Timber.i("PopulateRealmIntentService finished.")
        } catch (err: Exception) {
            Timber.e(err, "Error running PopulateRealmIntentService")
        }
    }

    private fun runOnUiThread(body: () -> Unit) {
        Handler(Looper.getMainLooper()).post { body.invoke() }
    }

}
