package com.andreapivetta.blu.data.db

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

    fun savePrivateMessage(privateMessage: PrivateMessage, body: (PrivateMessage) -> Unit = {})

    fun getAllUserFollowed(): List<UserFollowed>

    fun saveUserFollowed(userFollowed: UserFollowed, body: (UserFollowed) -> Unit = {})

    fun saveTweetInfo(tweetInfo: TweetInfo, body: (TweetInfo) -> Unit = {})

    fun saveMention(mention: Mention, body: (Mention) -> Unit = {})

    fun saveFollower(follower: Follower, body: (Follower) -> Unit = {})

    fun saveUserId(userId: UserId, body: (UserId) -> Unit = {})

    fun clear()

}