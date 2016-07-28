package com.andreapivetta.blu.data.db

import com.andreapivetta.blu.data.model.Notification
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.model.UserFollowed

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

    fun clear()

}