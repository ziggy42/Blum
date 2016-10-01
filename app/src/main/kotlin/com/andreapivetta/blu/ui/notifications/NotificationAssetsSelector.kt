package com.andreapivetta.blu.ui.notifications

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.model.Notification

/**
 * Created by andrea on 29/09/16.
 */
object NotificationAssetsSelector {

    fun getIcon(notification: Notification, context: Context): Drawable {
        when (notification.type) {
            Notification.FAVOURITE -> return ContextCompat
                    .getDrawable(context, R.drawable.ic_favorite)
            Notification.FOLLOW -> return ContextCompat
                    .getDrawable(context, R.drawable.ic_person_outline)
            Notification.MENTION -> return ContextCompat
                    .getDrawable(context, R.drawable.ic_reply)
            Notification.RETWEET -> return ContextCompat
                    .getDrawable(context, R.drawable.ic_repeat)
            else -> throw RuntimeException("No such notification type")
        }
    }

    fun getText(notification: Notification, context: Context): String {
        when (notification.type) {
            Notification.FAVOURITE -> return context.getString(R.string.like_not_title,
                    notification.userName, notification.status)
            Notification.FOLLOW -> return context.getString(R.string.is_following_not,
                    notification.userName)
            Notification.MENTION -> return context.getString(R.string.mentioned_not,
                    notification.userName, notification.status)
            Notification.RETWEET -> return context.getString(R.string.retw_not_title,
                    notification.userName, notification.status)
            else -> throw RuntimeException("No such notification type")
        }
    }

}