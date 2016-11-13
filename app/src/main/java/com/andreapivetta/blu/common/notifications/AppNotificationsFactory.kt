package com.andreapivetta.blu.common.notifications

import android.content.Context

/**
 * Created by andrea on 11/10/16.
 */
object AppNotificationsFactory {

    fun getAppNotifications(context: Context) = AndroidAppNotifications(context)
}