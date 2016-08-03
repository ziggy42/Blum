package com.andreapivetta.blu.data.db

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by andrea on 27/07/16.
 */
@RunWith(AndroidJUnit4::class)
class AppStorageImplTest {

    @Before
    fun setUp() {
        AppStorageImpl.testInit(InstrumentationRegistry.getTargetContext())
    }

    @After
    fun clean() {
        AppStorageImpl.clear()
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

        AppStorageImpl.saveNotification(notification1)
        AppStorageImpl.saveNotification(notification2)
        AppStorageImpl.saveNotification(notification3)
        AppStorageImpl.saveNotification(notification4)
        AppStorageImpl.saveNotification(notification5)
        assertEquals(5, AppStorageImpl.getAllNotifications().size)
        assertEquals(2, AppStorageImpl.getUnreadNotificationsCount())
        assertEquals(2, AppStorageImpl.getUnreadNotifications().size)

        val editableNotification = Notification(6, Notification.FAVOURITE, "username6", 6, 6,
                "status6", "profilepic6", false, Calendar.getInstance().timeInMillis)
        AppStorageImpl.saveNotification(editableNotification, {
            notification ->
            notification.userName = "Another Username"
        })
        val savedNotification = AppStorageImpl.getAllNotifications().last()
        assertEquals("Another Username", savedNotification.userName)
    }

    @Test
    fun testPrivateMessages() {
        val privateMessage1 = PrivateMessage(1)
        val privateMessage2 = PrivateMessage(2)
        val privateMessage3 = PrivateMessage(3)

        AppStorageImpl.savePrivateMessage(privateMessage1)
        AppStorageImpl.savePrivateMessage(privateMessage2)
        AppStorageImpl.savePrivateMessage(privateMessage3, { message -> message.isRead = true })
        assertEquals(3, AppStorageImpl.getAllPrivateMessages().size)
        assertEquals(2, AppStorageImpl.getUnreadPrivateMessages().size)
        assertEquals(2, AppStorageImpl.getUnreadPrivateMessagesCount())
    }

    @Test
    fun testUsersFollowed() {
        val userFollowed1 = UserFollowed(1)
        val userFollowed2 = UserFollowed(2)
        val userFollowed3 = UserFollowed(3)

        AppStorageImpl.saveUserFollowed(userFollowed1)
        AppStorageImpl.saveUserFollowed(userFollowed2)
        AppStorageImpl.saveUserFollowed(userFollowed3)
        assertEquals(3, AppStorageImpl.getAllUserFollowed().size)
    }

}