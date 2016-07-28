package com.andreapivetta.blu.data.model

import com.andreapivetta.blu.common.pref.AppSettingsImpl
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import twitter4j.DirectMessage

/**
 * Created by andrea on 26/07/16.
 */
open class PrivateMessage(@PrimaryKey open var id: Long = 0, open var senderId: Long = 0,
                          open var recipientId: Long = 0, open var otherId: Long = 0,
                          open var timeStamp: Long = 0, open var text: String = "",
                          open var otherUserName: String = "",
                          open var otherUserProfilePicUrl: String = "",
                          open var isRead: Boolean = false) : RealmObject(),
        Comparable<PrivateMessage> {

    companion object {
        fun valueOf(directMessage: DirectMessage): PrivateMessage {

            val loggedUserId = AppSettingsImpl.getLoggedUserId()
            val otherUsername = if (loggedUserId == directMessage.senderId)
                directMessage.recipientScreenName else directMessage.senderScreenName
            val otherUserProfilePicUrl: String = if (loggedUserId == directMessage.senderId)
                directMessage.recipient.biggerProfileImageURL else
                directMessage.sender.biggerProfileImageURL

            return PrivateMessage(directMessage.id, directMessage.senderId,
                    directMessage.recipientId, loggedUserId, directMessage.createdAt.time,
                    directMessage.text, otherUsername, otherUserProfilePicUrl, false)
        }
    }

    override fun compareTo(other: PrivateMessage) = (other.timeStamp - this.timeStamp).toInt()

}