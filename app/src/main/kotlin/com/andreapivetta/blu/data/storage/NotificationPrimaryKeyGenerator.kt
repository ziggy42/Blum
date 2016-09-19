package com.andreapivetta.blu.data.storage

import android.content.Context

/**
 * Created by andrea on 19/09/16.
 */
object NotificationPrimaryKeyGenerator {

    private var highestKey = 0L

    fun init(context: Context) {
        highestKey = AppStorageFactory.getAppStorage(context).getLastNotificationId() ?: 0
    }

    @Synchronized fun getNextKey() = highestKey++

}