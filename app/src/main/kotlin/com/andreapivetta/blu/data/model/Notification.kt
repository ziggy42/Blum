package com.andreapivetta.blu.data.model

import android.support.annotation.IntDef
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by andrea on 26/07/16.
 */
open class Notification(@PrimaryKey open var notificationID: Int = 0,
                        @NotificationType var type: Long = FOLLOW, var userName: String = "",
                        var userId: Long = 0, var tweetId: Long = 0, var status: String = "",
                        var profilePicURL: String = "", var isRead: Boolean = false,
                        var timestamp: Long = Calendar.getInstance().timeInMillis) : RealmObject() {

    companion object {

        @IntDef(FOLLOW, MENTION, FAVOURITE, RETWEET, RETWEET_MENTIONED)
        @Retention(AnnotationRetention.SOURCE)
        annotation class NotificationType

        const val FOLLOW = 0L
        const val MENTION = 1L
        const val FAVOURITE = 2L
        const val RETWEET = 3L
        const val RETWEET_MENTIONED = 4L
    }

}