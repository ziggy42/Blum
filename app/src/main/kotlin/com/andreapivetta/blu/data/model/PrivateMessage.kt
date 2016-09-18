package com.andreapivetta.blu.data.model

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import twitter4j.DirectMessage
import java.io.Serializable

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