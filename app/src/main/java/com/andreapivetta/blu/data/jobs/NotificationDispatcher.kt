package com.andreapivetta.blu.data.jobs

import android.content.Context
import android.content.Intent
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.notifications.AppNotificationsFactory
import com.andreapivetta.blu.data.model.Notification
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.data.storage.NotificationPrimaryKeyGenerator
import com.andreapivetta.blu.ui.main.MainActivity
import twitter4j.Status
import twitter4j.User

/**
 * Created by andrea on 18/09/16.
 */
class NotificationDispatcher(private val context: Context, private val storage: AppStorage) {

    private val keyGenerator = NotificationPrimaryKeyGenerator(storage)
    private val notifications = AppNotificationsFactory.getAppNotifications(context)

    fun sendFavoriteNotification(status: Status, user: User) {
        storage.saveNotification(Notification(keyGenerator.getNextKey(), Notification.FAVOURITE,
                user.name, user.id, status.id, status.text, user.biggerProfileImageURL, false))

        sendBroadcast(Notification.NEW_NOTIFICATION_INTENT)
        notifications.send(context.getString(R.string.notification_title_like, user.name), status.text,
                R.drawable.ic_favorite, user.biggerProfileImageURL, MainActivity::class.java)
    }

    fun sendRetweetNotification(status: Status, user: User) {
        storage.saveNotification(Notification(keyGenerator.getNextKey(), Notification.RETWEET,
                user.name, user.id, status.id, status.text, user.biggerProfileImageURL, false))

        sendBroadcast(Notification.NEW_NOTIFICATION_INTENT)
        notifications.send(context.getString(R.string.notification_title_retweet, user.name),
                status.text, R.drawable.ic_repeat, user.biggerProfileImageURL,
                MainActivity::class.java)
    }

    fun sendMentionNotification(status: Status, user: User) {
        storage.saveNotification(Notification(keyGenerator.getNextKey(), Notification.MENTION,
                user.name, user.id, status.id, status.text, user.biggerProfileImageURL, false))

        sendBroadcast(Notification.NEW_NOTIFICATION_INTENT)
        notifications.send(context.getString(R.string.reply_not_title, user.name), status.text,
                R.drawable.ic_reply, user.biggerProfileImageURL, MainActivity::class.java)
    }

    fun sendFollowerNotification(user: User) {
        storage.saveNotification(Notification(keyGenerator.getNextKey(), Notification.FAVOURITE,
                user.name, user.id, 0, "", user.biggerProfileImageURL, false))

        sendBroadcast(Notification.NEW_NOTIFICATION_INTENT)
        notifications.send(user.name, context.getString(R.string.follow_not_title, user.name),
                R.drawable.ic_person_outline, user.biggerProfileImageURL, MainActivity::class.java)
    }

    fun sendPrivateMessageNotification(privateMessage: PrivateMessage) {
        sendBroadcast(PrivateMessage.NEW_PRIVATE_MESSAGE_INTENT)
        notifications.send(context.getString(R.string.message_not_title,
                privateMessage.otherUserName), privateMessage.text, R.drawable.ic_message,
                privateMessage.otherUserProfilePicUrl, MainActivity::class.java)
    }

    private fun sendBroadcast(intentString: String) {
        context.sendBroadcast(Intent(intentString))
    }

}