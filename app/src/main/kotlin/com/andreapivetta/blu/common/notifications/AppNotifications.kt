package com.andreapivetta.blu.common.notifications

import android.app.PendingIntent
import android.support.annotation.DrawableRes

/**
 * Created by andrea on 11/10/16.
 */
interface AppNotifications {

    fun send(title: String, text: String, @DrawableRes icon: Int, largeIconUrl: String,
             pendingIntent: PendingIntent): Int

    fun sendLongRunning(title: String, text: String, @DrawableRes icon: Int): Int

    fun stopLongRunning(id: Int)
}