package com.andreapivetta.blu.common.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.ui.custom.Theme

/**
 * Created by andrea on 11/10/16.
 */
class AndroidAppNotifications(private val context: Context) : AppNotifications {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

    override fun send(title: String, text: String, @DrawableRes icon: Int, largeIconUrl: String,
                      intent: Intent): Int {
        val id = getId()
        notificationManager.notify(id, NotificationCompat.Builder(context)
                .setDefaults(android.app.Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setColor(Theme.getColorPrimary(context))
                .setLargeIcon(Utils.getBitmapFromURL(largeIconUrl))
                .setLights(Color.BLUE, 500, 1000)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(icon)
                .setContentIntent(getPendingIntent(context, intent))
                .build())
        return id
    }

    override fun sendLongRunning(title: String, text: String, @DrawableRes icon: Int): Int {
        val id = getId()
        notificationManager.notify(id, NotificationCompat.Builder(context)
                .setColor(Theme.getColorPrimary(context))
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(icon)
                .setProgress(0, 0, true)
                .build())
        return id
    }

    override fun stopLongRunning(id: Int) {
        notificationManager.cancel(id)
    }

    private fun getPendingIntent(context: Context, intent: Intent) = PendingIntent
            .getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    private fun getId() = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
}