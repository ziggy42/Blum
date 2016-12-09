package com.andreapivetta.blu.data.storage

/**
 * Created by andrea on 19/09/16.
 */
object NotificationPrimaryKeyGenerator {

    private var highestKey: Long = -1

    fun init(storage: AppStorage) {
        highestKey = storage.getLastNotificationId() ?: 0
    }

    @Synchronized fun getNextKey() = ++highestKey

}