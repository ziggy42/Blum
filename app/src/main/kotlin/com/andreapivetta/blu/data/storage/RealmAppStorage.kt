package com.andreapivetta.blu.data.storage

import android.content.Context
import com.andreapivetta.blu.data.model.*
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by andrea on 27/07/16.
 */
class RealmAppStorage(context: Context, name: String = "blumRealm") : AppStorage {

    private val SCHEMA_VERSION = 0L
    private val realm: Realm

    init {
        Realm.setDefaultConfiguration(RealmConfiguration.Builder(context)
                .name(name)
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

    override fun getAllFollowers(): List<Follower> = realm.where(Follower::class.java).findAll()

    override fun saveUserId(userId: UserId, body: (UserId) -> Unit) {
        realm.beginTransaction()
        body.invoke(userId)
        realm.copyToRealmOrUpdate(userId)
        realm.commitTransaction()
    }

    override fun saveTweetInfoList(tweetInfoList: List<TweetInfo>) {
        realm.beginTransaction()
        realm.copyToRealm(tweetInfoList)
        realm.commitTransaction()
    }

    override fun getAllTweetInfo(): List<TweetInfo> = realm.where(TweetInfo::class.java).findAll()

    override fun savePrivateMessages(privateMessages: List<PrivateMessage>) {
        realm.beginTransaction()
        realm.copyToRealm(privateMessages)
        realm.commitTransaction()
    }

    override fun saveUsersFollowed(userFollowed: List<UserFollowed>) {
        realm.beginTransaction()
        realm.copyToRealm(userFollowed)
        realm.commitTransaction()
    }

    override fun saveMentions(mentions: List<Mention>) {
        realm.beginTransaction()
        realm.copyToRealm(mentions)
        realm.commitTransaction()
    }

    override fun getAllMentions(): List<Mention> = realm.where(Mention::class.java).findAll()

    override fun saveFollowers(followers: List<Follower>) {
        realm.beginTransaction()
        realm.copyToRealm(followers)
        realm.commitTransaction()
    }

    override fun clear() {
        realm.beginTransaction()
        realm.delete(Follower::class.java)
        realm.delete(Mention::class.java)
        realm.delete(UserFollowed::class.java)
        realm.delete(TweetInfo::class.java)
        realm.delete(UserId::class.java)
        realm.delete(Notification::class.java)
        realm.delete(PrivateMessage::class.java)
        realm.commitTransaction()
    }

    override fun close() {
        realm.close()
    }
}