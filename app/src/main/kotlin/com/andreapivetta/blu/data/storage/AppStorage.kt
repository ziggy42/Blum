package com.andreapivetta.blu.data.storage

import com.andreapivetta.blu.data.model.*

/**
 * Created by andrea on 27/07/16.
 */
interface AppStorage {

    fun getAllNotifications(): List<Notification>

    fun getLastNotificationId(): Long?

    fun getReadNotifications(): List<Notification>

    fun getUnreadNotifications(): List<Notification>

    fun getUnreadNotificationsCount(): Long

    fun saveNotification(notification: Notification)

    fun markAllNotificationsAsRead()

    fun getAllPrivateMessages(): List<PrivateMessage>

    fun getUnreadPrivateMessagesCount(): Long

    fun getConversations(): MutableList<PrivateMessage>

    fun getConversation(otherUserId: Long): MutableList<PrivateMessage>

    fun setMessagesAsRead(messages: List<PrivateMessage>)

    fun savePrivateMessage(privateMessage: PrivateMessage)

    fun savePrivateMessages(privateMessages: List<PrivateMessage>)

    fun getAllUserFollowed(): List<UserFollowed>

    fun saveUserFollowed(userFollowed: UserFollowed)

    fun saveUsersFollowed(userFollowed: List<UserFollowed>)

    fun saveTweetInfo(tweetInfo: TweetInfo, body: (TweetInfo) -> Unit = {})

    fun saveTweetInfoList(tweetInfoList: List<TweetInfo>)

    fun getAllTweetInfo(): List<TweetInfo>

    fun saveMention(mention: Mention)

    fun saveMentions(mentions: List<Mention>)

    fun getAllMentions(): List<Mention>

    fun saveFollower(follower: Follower)

    fun saveFollowers(followers: List<Follower>)

    fun getAllFollowers(): List<Follower>

    fun clear()

    fun close()

}