package com.andreapivetta.blu.data.storage

/**
 * Created by andrea on 19/09/16.
 */
class NotificationPrimaryKeyGenerator(storage: AppStorage) {

    private var highestKey = storage.getLastNotificationId() ?: 0

    @Synchronized fun getNextKey() = highestKey++

}