package com.andreapivetta.blu.data.jobs

import android.content.Context
import android.content.Intent
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.notifications.AppNotificationsFactory
import com.andreapivetta.blu.data.model.Notification
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.data.storage.NotificationPrimaryKeyGenerator
import com.andreapivetta.blu.ui.conversation.ConversationActivity
import com.andreapivetta.blu.ui.profile.UserActivity
import com.andreapivetta.blu.ui.tweetdetails.TweetDetailsActivity
import twitter4j.Status
import twitter4j.User

/**
 * Created by andrea on 18/09/16.
 */
class NotificationDispatcher(private val context: Context, private val storage: AppStorage) {

    private val notifications = AppNotificationsFactory.getAppNotifications(context)

    fun sendFavoriteNotification(status: Status, user: User) {
        storage.saveNotification(Notification(NotificationPrimaryKeyGenerator.getNextKey(), Notification.FAVOURITE,
                user.name, user.id, status.id, status.text, user.biggerProfileImageURL))

        sendBroadcast(Notification.NEW_NOTIFICATION_INTENT)
        notifications.send(context.getString(R.string.notification_title_like, user.name),
                status.text, R.drawable.ic_favorite, user.biggerProfileImageURL,
                getTweetDetailsIntent(status))
    }

    fun sendRetweetNotification(status: Status, user: User) {
        storage.saveNotification(Notification(NotificationPrimaryKeyGenerator.getNextKey(), Notification.RETWEET,
                user.name, user.id, status.id, status.text, user.biggerProfileImageURL))

        sendBroadcast(Notification.NEW_NOTIFICATION_INTENT)
        notifications.send(context.getString(R.string.notification_title_retweet, user.name),
                status.text, R.drawable.ic_repeat, user.biggerProfileImageURL,
                getTweetDetailsIntent(status))
    }

    fun sendMentionNotification(status: Status, user: User) {
        storage.saveNotification(Notification(NotificationPrimaryKeyGenerator.getNextKey(), Notification.MENTION,
                user.name, user.id, status.id, status.text, user.biggerProfileImageURL))

        sendBroadcast(Notification.NEW_NOTIFICATION_INTENT)
        notifications.send(context.getString(R.string.reply_not_title, user.name), status.text,
                R.drawable.ic_reply, user.biggerProfileImageURL, getTweetDetailsIntent(status))
    }

    fun sendFollowerNotification(user: User) {
        val intent = Intent(context, UserActivity::class.java)
        intent.putExtra(UserActivity.TAG_USER, user)

        storage.saveNotification(Notification(NotificationPrimaryKeyGenerator.getNextKey(), Notification.FOLLOW,
                user.name, user.id, 0, "", user.biggerProfileImageURL))

        sendBroadcast(Notification.NEW_NOTIFICATION_INTENT)
        notifications.send(user.name, context.getString(R.string.follow_not_title, user.name),
                R.drawable.ic_person_outline, user.biggerProfileImageURL, intent)
    }

    fun sendPrivateMessageNotification(message: PrivateMessage) {
        val intent = Intent(context, ConversationActivity::class.java)
        intent.putExtra(ConversationActivity.ARG_OTHER_ID, message.otherId)

        sendBroadcast(PrivateMessage.NEW_PRIVATE_MESSAGE_INTENT)
        notifications.send(context.getString(R.string.message_not_title, message.otherUserName),
                message.text, R.drawable.ic_message, message.otherUserProfilePicUrl, intent)
    }

    private fun sendBroadcast(intentString: String) {
        context.sendBroadcast(Intent(intentString))
    }

    private fun getTweetDetailsIntent(status: Status): Intent {
        val intent = Intent(context, TweetDetailsActivity::class.java)
        intent.putExtra(TweetDetailsActivity.TAG_ID, status.id)
        return intent
    }

}