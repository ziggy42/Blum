package com.andreapivetta.blu.data.jobs

import android.content.Context
import com.andreapivetta.blu.data.model.PrivateMessage
import timber.log.Timber
import twitter4j.Status
import twitter4j.User

/**
 * Created by andrea on 18/09/16.
 */
class NotificationDispatcher(val context: Context) {

    fun sendFavoriteNotification(status: Status, user: User) {
        Timber.i("Sending notification for new favorite")
    }

    fun sendRetweetNotification(status: Status, user: User) {
        Timber.i("Sending notification for new retweet")
    }

    fun sendMentionNotification(status: Status, user: User) {
        Timber.i("Sending notification for new mention")
    }

    fun sendFollowerNotification(user: User) {
        Timber.i("Sending notification for new follower")
    }

    fun sendPrivateMessageNotification(privateMessage: PrivateMessage) {
        Timber.i("Sending notification for new private message")
    }

}