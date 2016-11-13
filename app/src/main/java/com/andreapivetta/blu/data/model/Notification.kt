package com.andreapivetta.blu.data.model

import android.support.annotation.IntDef
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Notification(
        @PrimaryKey open var id: Long = 0,
        @NotificationType var type: Long = FOLLOW,
        var userName: String = "",
        var userId: Long = 0,
        var tweetId: Long = 0,
        var status: String = "",
        var profilePicURL: String = "",
        var isRead: Boolean = false,
        var timestamp: Long = System.currentTimeMillis()) :
        RealmObject() {

    companion object {

        val NEW_NOTIFICATION_INTENT = "com.andreapivetta.blu.data.model.NEW_NOTIFICATION_INTENT"

        @IntDef(FOLLOW, MENTION, FAVOURITE, RETWEET)
        @Retention(AnnotationRetention.SOURCE)
        annotation class NotificationType

        const val FOLLOW = 0L
        const val MENTION = 1L
        const val FAVOURITE = 2L
        const val RETWEET = 3L
    }

}