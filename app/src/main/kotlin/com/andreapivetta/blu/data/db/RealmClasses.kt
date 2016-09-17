package com.andreapivetta.blu.data.db

import android.support.annotation.IntDef
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import twitter4j.DirectMessage
import twitter4j.Status
import twitter4j.User
import java.io.Serializable
import java.util.*

/**
 * Created by andrea on 01/08/16.
 */
open class TweetInfo(
        @PrimaryKey
        open var id: Long = 0,
        open var favoriters: RealmList<UserId>? = RealmList<UserId>(),
        open var retweeters: RealmList<UserId>? = RealmList<UserId>()
) : RealmObject()

open class Mention(
        open var tweetId: Long = 0,
        open var userId: Long = 0,
        open var timestamp: Long = 0) : RealmObject() {

    companion object {
        fun valueOf(status: Status) = Mention(status.id, status.user.id, status.createdAt.time)
    }

}

open class UserId(
        @PrimaryKey
        open var userId: Long = 0
) : RealmObject()

open class Follower(
        @PrimaryKey
        open var userId: Long = 0
) : RealmObject()

open class UserFollowed(
        @PrimaryKey open var id: Long = 0,
        open var name: String = "",
        open var screenName: String = "",
        open var profilePicUrl: String = "") : RealmObject() {

    companion object {
        fun valueOf(user: User) =
                UserFollowed(user.id, user.name, user.screenName, user.biggerProfileImageURL)
    }

}

open class Notification(
        @PrimaryKey open var notificationID: Int = 0,
        @NotificationType var type: Long = FOLLOW,
        var userName: String = "",
        var userId: Long = 0,
        var tweetId: Long = 0,
        var status: String = "",
        var profilePicURL: String = "",
        var isRead: Boolean = false,
        var timestamp: Long = Calendar.getInstance().timeInMillis) :
        RealmObject() {

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

open class PrivateMessage(
        @PrimaryKey open var id: Long = 0,
        open var senderId: Long = 0,
        open var recipientId: Long = 0,
        @Index open var otherId: Long = 0,
        open var timeStamp: Long = 0,
        open var text: String = "",
        open var otherUserName: String = "",
        open var otherUserProfilePicUrl: String = "",
        open var isRead: Boolean = false) :
        RealmObject(), Comparable<PrivateMessage>, Serializable {

    companion object {
        fun valueOf(directMessage: DirectMessage, loggedUserId: Long): PrivateMessage {

            val otherUserId = if (directMessage.recipientId == loggedUserId)
                directMessage.senderId else directMessage.recipientId
            val otherUsername = if (loggedUserId == directMessage.senderId)
                directMessage.recipientScreenName else directMessage.senderScreenName
            val otherUserProfilePicUrl: String = if (loggedUserId == directMessage.senderId)
                directMessage.recipient.biggerProfileImageURL else
                directMessage.sender.biggerProfileImageURL

            return PrivateMessage(directMessage.id, directMessage.senderId,
                    directMessage.recipientId, otherUserId, directMessage.createdAt.time,
                    directMessage.text, otherUsername, otherUserProfilePicUrl, false)
        }
    }

    override fun compareTo(other: PrivateMessage) = (other.timeStamp - this.timeStamp).toInt()

}

