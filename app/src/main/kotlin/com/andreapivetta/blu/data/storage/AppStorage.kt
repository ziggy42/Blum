package com.andreapivetta.blu.data.storage

import com.andreapivetta.blu.data.model.*

/**
 * Created by andrea on 27/07/16.
 */
interface AppStorage {

    fun getAllNotifications(): List<Notification>

    fun getUnreadNotifications(): List<Notification>

    fun getUnreadNotificationsCount(): Long

    fun saveNotification(notification: Notification, body: (Notification) -> Unit = {})

    fun getAllPrivateMessages(): List<PrivateMessage>

    fun getUnreadPrivateMessages(): List<PrivateMessage>

    fun getUnreadPrivateMessagesCount(): Long

    fun getConversations(): MutableList<PrivateMessage>

    fun savePrivateMessage(privateMessage: PrivateMessage, body: (PrivateMessage) -> Unit = {})

    fun savePrivateMessages(privateMessages: List<PrivateMessage>)

    fun getAllUserFollowed(): List<UserFollowed>

    fun saveUserFollowed(userFollowed: UserFollowed, body: (UserFollowed) -> Unit = {})

    fun saveUsersFollowed(userFollowed: List<UserFollowed>)

    fun saveTweetInfo(tweetInfo: TweetInfo, body: (TweetInfo) -> Unit = {})

    fun saveTweetInfoList(tweetInfoList: List<TweetInfo>)

    fun getAllTweetInfo(): List<TweetInfo>

    fun saveMention(mention: Mention, body: (Mention) -> Unit = {})

    fun saveMentions(mentions: List<Mention>)

    fun getAllMentions(): List<Mention>

    fun saveFollower(follower: Follower, body: (Follower) -> Unit = {})

    fun saveFollowers(followers: List<Follower>)

    fun getAllFollowers(): List<Follower>

    fun saveUserId(userId: UserId, body: (UserId) -> Unit = {})

    fun clear()

    fun close()

}