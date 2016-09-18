package com.andreapivetta.blu.data.storage

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.andreapivetta.blu.data.model.Notification
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.model.UserFollowed
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by andrea on 27/07/16.
 */
@RunWith(AndroidJUnit4::class)
class RealmAppStorageTest {

    private val realmAppStorage = RealmAppStorage(InstrumentationRegistry.getTargetContext(), "test")

    @After
    fun clear() {
        realmAppStorage.clear()
    }

    @Test
    fun testNotifications() {
        val notification1 = Notification(1, Notification.FAVOURITE, "username1", 1, 1, "status1",
                "profilepic1", false, Calendar.getInstance().timeInMillis)
        val notification2 = Notification(2, Notification.FOLLOW, "username2", 2, 2, "status2",
                "profilepic2", true, Calendar.getInstance().timeInMillis)
        val notification3 = Notification(3, Notification.MENTION, "username3", 3, 3, "status3",
                "profilepic3", false, Calendar.getInstance().timeInMillis)
        val notification4 = Notification(4, Notification.RETWEET, "username4", 4, 4, "status4",
                "profilepic4", true, Calendar.getInstance().timeInMillis)
        val notification5 = Notification(5, Notification.RETWEET_MENTIONED, "username5", 5, 5,
                "status5", "profilepic5", false, Calendar.getInstance().timeInMillis)

        realmAppStorage.saveNotification(notification1)
        realmAppStorage.saveNotification(notification2)
        realmAppStorage.saveNotification(notification3)
        realmAppStorage.saveNotification(notification4)
        realmAppStorage.saveNotification(notification5)
        assertEquals(5, realmAppStorage.getAllNotifications().size)
        assertEquals(2, realmAppStorage.getUnreadNotificationsCount())
        assertEquals(2, realmAppStorage.getUnreadNotifications().size)

        val editableNotification = Notification(6, Notification.FAVOURITE, "username6", 6, 6,
                "status6", "profilepic6", false, Calendar.getInstance().timeInMillis)
        realmAppStorage.saveNotification(editableNotification, { x ->
            x.userName = "Another Username"
        })
        val savedNotification = realmAppStorage.getAllNotifications().last()
        assertEquals("Another Username", savedNotification.userName)
    }

    @Test
    fun testPrivateMessages() {
        val privateMessage1 = PrivateMessage(1)
        val privateMessage2 = PrivateMessage(2)
        val privateMessage3 = PrivateMessage(3)

        realmAppStorage.savePrivateMessage(privateMessage1)
        realmAppStorage.savePrivateMessage(privateMessage2)
        realmAppStorage.savePrivateMessage(privateMessage3, { message -> message.isRead = true })
        assertEquals(3, realmAppStorage.getAllPrivateMessages().size)
        assertEquals(2, realmAppStorage.getUnreadPrivateMessages().size)
        assertEquals(2, realmAppStorage.getUnreadPrivateMessagesCount())
    }

    @Test
    fun testUsersFollowed() {
        val userFollowed1 = UserFollowed(1)
        val userFollowed2 = UserFollowed(2)
        val userFollowed3 = UserFollowed(3)

        realmAppStorage.saveUserFollowed(userFollowed1)
        realmAppStorage.saveUserFollowed(userFollowed2)
        realmAppStorage.saveUserFollowed(userFollowed3)
        assertEquals(3, realmAppStorage.getAllUserFollowed().size)
    }

}