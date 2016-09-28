package com.andreapivetta.blu.data.storage

/**
 * Created by andrea on 19/09/16.
 */
object NotificationPrimaryKeyGenerator {

    private var highestKey = AppStorageFactory.getAppStorage().getLastNotificationId() ?: 0

    @Synchronized fun getNextKey() = highestKey++

}