package com.andreapivetta.blu.data.jobs

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
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
class NotificationDispatcher(val context: Context, val storage: AppStorage) {

    fun sendFavoriteNotification(status: Status, user: User) {
        storage.saveNotification(Notification(NotificationPrimaryKeyGenerator.getNextKey(),
                Notification.FAVOURITE, user.name, user.id, status.id, status.text,
                user.biggerProfileImageURL, false))

        context.sendBroadcast(Intent(Notification.NEW_NOTIFICATION_INTENT))

        val pendingIntent = PendingIntent.getActivity(context, 0,
                Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        sendNotification((System.currentTimeMillis().toInt()) * 2,
                context.getString(R.string.like_not_title, user.name), status.text,
                R.drawable.ic_favorite, user.biggerProfileImageURL, pendingIntent)
    }

    fun sendRetweetNotification(status: Status, user: User) {
        storage.saveNotification(Notification(NotificationPrimaryKeyGenerator.getNextKey(),
                Notification.RETWEET, user.name, user.id, status.id, status.text,
                user.biggerProfileImageURL, false))

        context.sendBroadcast(Intent(Notification.NEW_NOTIFICATION_INTENT))

        val pendingIntent = PendingIntent.getActivity(context, 0,
                Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        sendNotification((System.currentTimeMillis().toInt()) * 2,
                context.getString(R.string.retw_not_title, user.name), status.text,
                R.drawable.ic_repeat, user.biggerProfileImageURL, pendingIntent)
    }

    fun sendMentionNotification(status: Status, user: User) {
        storage.saveNotification(Notification(NotificationPrimaryKeyGenerator.getNextKey(),
                Notification.MENTION, user.name, user.id, status.id, status.text,
                user.biggerProfileImageURL, false))

        context.sendBroadcast(Intent(Notification.NEW_NOTIFICATION_INTENT))

        val pendingIntent = PendingIntent.getActivity(context, 0,
                Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        sendNotification(status.id.toInt(), context.getString(R.string.reply_not_title, user.name),
                status.text, R.drawable.ic_reply, user.biggerProfileImageURL, pendingIntent)
    }

    fun sendFollowerNotification(user: User) {
        storage.saveNotification(Notification(NotificationPrimaryKeyGenerator.getNextKey(),
                Notification.FAVOURITE, user.name, user.id, 0, "", user.biggerProfileImageURL,
                false))

        context.sendBroadcast(Intent(Notification.NEW_NOTIFICATION_INTENT))

        val pendingIntent = PendingIntent.getActivity(context, 0,
                Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        sendNotification(user.id.toInt(), user.name, context.getString(R.string.follow_not_title,
                user.name), R.drawable.ic_person_outline, user.biggerProfileImageURL, pendingIntent)
    }

    fun sendPrivateMessageNotification(privateMessage: PrivateMessage) {
        context.sendBroadcast(Intent(PrivateMessage.NEW_PRIVATE_MESSAGE_INTENT))

        val pendingIntent = PendingIntent.getActivity(context, 0,
                Intent(context, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        sendNotification(privateMessage.id.toInt(), context.getString(R.string.message_not_title,
                privateMessage.otherUserName), privateMessage.text, R.drawable.ic_message,
                privateMessage.otherUserProfilePicUrl, pendingIntent)
    }

    private fun sendNotification(id: Int, title: String, text: String, @DrawableRes icon: Int,
                                 largeIconUrl: String, pendingIntent: PendingIntent) {
        val notification = NotificationCompat.Builder(context)
                .setDefaults(android.app.Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setColor(Utils.getResourceColorPrimary(context))
                .setLargeIcon(Utils.getBitmapFromURL(largeIconUrl))
                .setLights(Color.BLUE, 500, 1000)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(icon)
                .setContentIntent(pendingIntent)
                .build()

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .notify(id, notification)
    }

}