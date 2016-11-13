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
        open var timeStamp: Long = System.currentTimeMillis(),
        open var text: String = "",
        open var otherUserName: String = "",
        open var otherUserProfilePicUrl: String = "",
        open var isRead: Boolean = false) :
        RealmObject(), Comparable<PrivateMessage>, Serializable {

    companion object {

        val NEW_PRIVATE_MESSAGE_INTENT =
                "com.andreapivetta.blu.data.model.NEW_PRIVATE_MESSAGE_INTENT"

        fun valueOf(directMessage: DirectMessage, userId: Long, inverted: Boolean = false):
                PrivateMessage {
            if (inverted)
                return getInverted(directMessage, userId)

            val otherUserId = if (directMessage.recipientId == userId)
                directMessage.senderId else directMessage.recipientId
            val otherUsername = if (userId == directMessage.senderId)
                directMessage.recipientScreenName else directMessage.senderScreenName
            val otherUserProfilePicUrl: String = if (userId == directMessage.senderId)
                directMessage.recipient.biggerProfileImageURL else
                directMessage.sender.biggerProfileImageURL

            return PrivateMessage(directMessage.id, directMessage.senderId,
                    directMessage.recipientId, otherUserId, directMessage.createdAt.time,
                    directMessage.text, otherUsername, otherUserProfilePicUrl, false)
        }

        private fun getInverted(directMessage: DirectMessage, userId: Long): PrivateMessage {
            val otherUserId = if (directMessage.recipientId != userId)
                directMessage.senderId else directMessage.recipientId
            val otherUsername = if (userId != directMessage.senderId)
                directMessage.recipientScreenName else directMessage.senderScreenName
            val otherUserProfilePicUrl: String = if (userId != directMessage.senderId)
                directMessage.recipient.biggerProfileImageURL else
                directMessage.sender.biggerProfileImageURL

            return PrivateMessage(directMessage.id, directMessage.senderId,
                    directMessage.recipientId, otherUserId, directMessage.createdAt.time,
                    directMessage.text, otherUsername, otherUserProfilePicUrl, false)
        }
    }

    override fun compareTo(other: PrivateMessage) = (other.timeStamp - this.timeStamp).toInt()

}