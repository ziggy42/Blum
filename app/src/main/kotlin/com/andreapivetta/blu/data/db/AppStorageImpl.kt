package com.andreapivetta.blu.data.db

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by andrea on 27/07/16.
 */
object AppStorageImpl : AppStorage {

    private val SCHEMA_VERSION = 0L
    private lateinit var realm: Realm

    fun init(context: Context) {
        Realm.setDefaultConfiguration(RealmConfiguration.Builder(context)
                .schemaVersion(SCHEMA_VERSION).build())
        realm = Realm.getDefaultInstance()
    }

    // Should be called only in tests
    fun testInit(context: Context) {
        Realm.setDefaultConfiguration(RealmConfiguration.Builder(context)
                .name("test")
                .schemaVersion(SCHEMA_VERSION).build())
        realm = Realm.getDefaultInstance()
    }

    override fun getAllNotifications(): List<Notification> =
            realm.where(Notification::class.java).findAll()

    override fun getUnreadNotifications(): List<Notification> = realm
            .where(Notification::class.java).equalTo("isRead", true).findAll()

    override fun getUnreadNotificationsCount(): Long = realm.where(Notification::class.java)
            .equalTo("isRead", true).count()

    override fun saveNotification(notification: Notification, body: (Notification) -> Unit) {
        realm.beginTransaction()
        body.invoke(notification)
        realm.copyToRealm(notification)
        realm.commitTransaction()
    }

    override fun getAllPrivateMessages(): List<PrivateMessage> =
            realm.where(PrivateMessage::class.java).findAll()

    override fun getUnreadPrivateMessages(): List<PrivateMessage> = realm
            .where(PrivateMessage::class.java).equalTo("isRead", false).findAll()

    override fun getConversations(): MutableList<PrivateMessage> = realm
            .where(PrivateMessage::class.java).findAllSorted("timeStamp").distinct("otherId")

    override fun getUnreadPrivateMessagesCount(): Long = realm
            .where(PrivateMessage::class.java).equalTo("isRead", false).count()

    override fun savePrivateMessage(privateMessage: PrivateMessage,
                                    body: (PrivateMessage) -> Unit) {
        realm.beginTransaction()
        body.invoke(privateMessage)
        realm.copyToRealm(privateMessage)
        realm.commitTransaction()
    }

    override fun getAllUserFollowed(): List<UserFollowed> = realm
            .where(UserFollowed::class.java).findAll()

    override fun saveUserFollowed(userFollowed: UserFollowed, body: (UserFollowed) -> Unit) {
        realm.beginTransaction()
        body.invoke(userFollowed)
        realm.copyToRealm(userFollowed)
        realm.commitTransaction()
    }

    override fun saveTweetInfo(tweetInfo: TweetInfo, body: (TweetInfo) -> Unit) {
        realm.beginTransaction()
        body.invoke(tweetInfo)
        realm.copyToRealmOrUpdate(tweetInfo)
        realm.commitTransaction()
    }

    override fun saveMention(mention: Mention, body: (Mention) -> Unit) {
        realm.beginTransaction()
        body.invoke(mention)
        realm.copyToRealm(mention)
        realm.commitTransaction()
    }

    override fun saveFollower(follower: Follower, body: (Follower) -> Unit) {
        realm.beginTransaction()
        body.invoke(follower)
        realm.copyToRealm(follower)
        realm.commitTransaction()
    }

    override fun saveUserId(userId: UserId, body: (UserId) -> Unit) {
        realm.beginTransaction()
        body.invoke(userId)
        realm.copyToRealmOrUpdate(userId)
        realm.commitTransaction()
    }

    override fun clear() {
        realm.close()
        Realm.deleteRealm(realm.configuration)
    }

}