package com.andreapivetta.blu.data.storage

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.andreapivetta.blu.data.model.*
import io.realm.Realm
import io.realm.RealmList
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by andrea on 23/09/16.
 */
@RunWith(AndroidJUnit4::class)
class RealmAppStorageTest {

    init {
        Realm.init(InstrumentationRegistry.getTargetContext())
    }

    private val storage = RealmAppStorage("test")
    private val currentUserId = 10L

    @Before
    fun setup() {
        storage.saveNotification(Notification(1, Notification.FAVOURITE, "name1", 1, 1,
                "status1", "pic1", false, System.currentTimeMillis() + 60))
        storage.saveNotification(Notification(2, Notification.FOLLOW, "name2", 2, 2, "status2",
                "pic2", true, System.currentTimeMillis() + 300))
        storage.saveNotification(Notification(3, Notification.MENTION, "name3", 3, 3, "status3",
                "pic3", false, System.currentTimeMillis()))
        storage.saveNotification(Notification(4, Notification.RETWEET, "name4", 4, 4, "status4",
                "pic4", true, System.currentTimeMillis() + 30))

        storage.savePrivateMessage(PrivateMessage(1, currentUserId, 3, 3, 1, "ciao", isRead = true))
        storage.savePrivateMessage(PrivateMessage(2, 3, currentUserId, 3, 2, "ciao a te"))
        storage.savePrivateMessage(PrivateMessage(3, currentUserId, 3, 3, 3, "ok"))
        storage.savePrivateMessage(PrivateMessage(4, 7, currentUserId, 7, 4, "mmm"))

        storage.saveUserFollowed(UserFollowed(1, "a"))
        storage.saveUserFollowed(UserFollowed(2, "c"))
        storage.saveUserFollowed(UserFollowed(3, "d"))
        storage.saveUserFollowed(UserFollowed(4, "b"))

        storage.saveTweetInfo(TweetInfo(1, RealmList(), RealmList()))
        storage.saveTweetInfo(TweetInfo(2, RealmList(), RealmList()))
        storage.saveTweetInfo(TweetInfo(3, RealmList(), RealmList()))
        storage.saveTweetInfo(TweetInfo(4, RealmList(), RealmList()))

        storage.saveMention(Mention(0, 0, System.currentTimeMillis()))
        storage.saveMention(Mention(0, 1, System.currentTimeMillis() + 10))
        storage.saveMention(Mention(1, 2, System.currentTimeMillis() + 20))
        storage.saveMention(Mention(2, 0, System.currentTimeMillis() + 30))

        storage.saveFollower(Follower(0))
        storage.saveFollower(Follower(1))
        storage.saveFollower(Follower(2))
        storage.saveFollower(Follower(3))
    }

    @After
    fun clean() {
        storage.clear()
    }

    @Test
    fun getAllNotifications() {
        val notifications = storage.getAllNotifications()
        assertEquals(4, notifications.size)
        assertEquals(2, notifications[0].id)
        assertEquals(3, notifications[3].id)
    }

    @Test
    fun getLastNotificationId() {
        assertEquals(4L, storage.getLastNotificationId())
    }

    @Test
    fun getUnreadNotifications() {
        val notifications = storage.getUnreadNotifications()
        assertEquals(2, notifications.size)
        assertEquals(1, notifications[0].id)
        assertEquals(3, notifications[1].id)
    }

    @Test
    fun getUnreadNotificationsCount() {
        assertEquals(2, storage.getUnreadNotificationsCount())
    }

    @Test
    fun saveNotification() {
        storage.saveNotification(Notification(100, Notification.FAVOURITE, "nameN", 1, 1,
                "statusN", "picN", false, System.currentTimeMillis()))
        assertEquals(5, storage.getAllNotifications().size)
    }

    @Test
    fun getAllPrivateMessages() {
        val privateMessages = storage.getAllPrivateMessages()
        assertEquals(4, privateMessages.size)
    }

    @Test
    fun getConversations() {
        val privateMessages = storage.getConversations()
        assertEquals(2, privateMessages.size)
        assertEquals(7, privateMessages[0].otherId)
        assertEquals(3, privateMessages[1].otherId)
        assertEquals("mmm", privateMessages[0].text)
        assertEquals("ok", privateMessages[1].text)
    }

    @Test
    fun getConversation() {
        val privateMessages = storage.getConversation(3)
        assertEquals(3, privateMessages.size)
        assertEquals("ok", privateMessages[0].text)
    }

    @Test
    fun getUnreadPrivateMessagesCount() {
        assertEquals(3, storage.getUnreadPrivateMessagesCount())
    }

    @Test
    fun savePrivateMessage() {
        storage.savePrivateMessage(PrivateMessage())
        assertEquals(5, storage.getAllPrivateMessages().size)
    }

    @Test
    fun getAllUserFollowed() {
        val usersFollowed = storage.getAllUserFollowed()
        assertEquals(4, usersFollowed.size)
        assertEquals(1, usersFollowed[0].id)
        assertEquals(4, usersFollowed[1].id)
        assertEquals(2, usersFollowed[2].id)
        assertEquals(3, usersFollowed[3].id)
    }

    @Test
    fun saveUserFollowed() {
        storage.saveUserFollowed(UserFollowed())
        assertEquals(5, storage.getAllUserFollowed().size)
    }

    @Test
    fun saveTweetInfo() {
        storage.saveTweetInfo(TweetInfo(5))
        assertEquals(5, storage.getAllTweetInfo().size)
    }

    @Test
    fun saveMention() {
        storage.saveMention(Mention(5))
        assertEquals(5, storage.getAllMentions().size)
    }

    @Test
    fun saveFollower() {
        storage.saveFollower(Follower(5))
        assertEquals(5, storage.getAllFollowers().size)
    }

    @Test
    fun getAllFollowers() {
        assertEquals(4, storage.getAllFollowers().size)
    }

    @Test
    fun saveTweetInfoList() {
        storage.saveTweetInfo(TweetInfo(5))
        assertEquals(5, storage.getAllTweetInfo().size)
    }

    @Test
    fun getAllTweetInfo() {
        assertEquals(4, storage.getAllTweetInfo().size)
    }

    @Test
    fun savePrivateMessages() {
        storage.savePrivateMessage(PrivateMessage(60))
        assertEquals(5, storage.getAllPrivateMessages().size)
    }

    @Test
    fun saveUsersFollowed() {
        storage.saveUserFollowed(UserFollowed(90))
        assertEquals(5, storage.getAllUserFollowed().size)
    }

    @Test
    fun saveMentions() {
        storage.saveMention(Mention(90))
        assertEquals(5, storage.getAllMentions().size)
    }

    @Test
    fun getAllMentions() {
        val mentions = storage.getAllMentions()
        assertEquals(0, mentions[0].tweetId)
        assertEquals(0, mentions[1].tweetId)
        assertEquals(1, mentions[2].tweetId)
        assertEquals(2, mentions[3].tweetId)
        assertEquals(0, mentions[0].userId)
        assertEquals(1, mentions[1].userId)
        assertEquals(2, mentions[2].userId)
        assertEquals(0, mentions[3].userId)
    }

    @Test
    fun saveFollowers() {
        storage.saveFollowers(Arrays.asList(Follower(100), Follower(101)))
        assertEquals(6, storage.getAllFollowers().size)
    }

    @Test
    fun clear() {
        storage.clear()
        assertEquals(0, storage.getAllFollowers().size)
        assertEquals(0, storage.getAllMentions().size)
        assertEquals(0, storage.getAllUserFollowed().size)
        assertEquals(0, storage.getAllTweetInfo().size)
        assertEquals(0, storage.getAllNotifications().size)
        assertEquals(0, storage.getAllPrivateMessages().size)
    }

}