package com.andreapivetta.blu.data.storage

import com.andreapivetta.blu.data.model.*
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort

/**
 * Created by andrea on 27/07/16.
 */
class RealmAppStorage(name: String = "blumRealm") : AppStorage {

    private val SCHEMA_VERSION = 0L
    private val realm: Realm

    init {
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .name(name)
                .schemaVersion(SCHEMA_VERSION).build())
        realm = Realm.getDefaultInstance()
    }

    override fun getAllNotifications(): List<Notification> = realm.where(Notification::class.java)
            .findAllSorted("timestamp", Sort.DESCENDING)

    override fun getLastNotificationId(): Long? =
            realm.where(Notification::class.java).max("id")?.toLong()

    override fun getReadNotifications(): List<Notification> = realm
            .where(Notification::class.java).equalTo("isRead", true)
            .findAllSorted("timestamp", Sort.DESCENDING)

    override fun getUnreadNotifications(): List<Notification> = realm
            .where(Notification::class.java).equalTo("isRead", false)
            .findAllSorted("timestamp", Sort.DESCENDING)

    override fun getUnreadNotificationsCount(): Long = realm.where(Notification::class.java)
            .equalTo("isRead", false).count()

    override fun saveNotification(notification: Notification, body: (Notification) -> Unit) {
        realm.executeTransaction {
            body.invoke(notification)
            realm.copyToRealm(notification)
        }
    }

    override fun markAllNotificationsAsRead() {
        realm.executeTransaction {
            realm.where(Notification::class.java).equalTo("isRead", false).findAll()
                    .forEach { x -> x.isRead = true }
        }
    }

    // it doesn't matter they're not sorted
    override fun getAllPrivateMessages(): List<PrivateMessage> =
            realm.where(PrivateMessage::class.java).findAll()

    // Workaround https://github.com/realm/realm-java/issues/3503
    override fun getConversations(): MutableList<PrivateMessage> = realm
            .where(PrivateMessage::class.java)
            .findAllSorted("timeStamp", Sort.DESCENDING)
            .distinctBy { x -> x.otherId }.toMutableList()

    override fun getConversation(otherUserId: Long): MutableList<PrivateMessage> = realm
            .where(PrivateMessage::class.java)
            .equalTo("otherId", otherUserId)
            .findAllSorted("timeStamp")

    override fun getUnreadPrivateMessagesCount(): Long = realm
            .where(PrivateMessage::class.java).equalTo("isRead", false).count()

    override fun savePrivateMessage(privateMessage: PrivateMessage,
                                    body: (PrivateMessage) -> Unit) {
        realm.executeTransaction {
            body.invoke(privateMessage)
            realm.copyToRealm(privateMessage)
        }
    }

    override fun getAllUserFollowed(): List<UserFollowed> = realm
            .where(UserFollowed::class.java).findAllSorted("name")

    override fun saveUserFollowed(userFollowed: UserFollowed, body: (UserFollowed) -> Unit) {
        realm.executeTransaction {
            body.invoke(userFollowed)
            realm.copyToRealm(userFollowed)
        }
    }

    override fun saveTweetInfo(tweetInfo: TweetInfo, body: (TweetInfo) -> Unit) {
        realm.executeTransaction {
            body.invoke(tweetInfo)
            realm.copyToRealmOrUpdate(tweetInfo)
        }
    }

    override fun saveMention(mention: Mention, body: (Mention) -> Unit) {
        realm.executeTransaction {
            body.invoke(mention)
            realm.copyToRealm(mention)
        }
    }

    override fun saveFollower(follower: Follower, body: (Follower) -> Unit) {
        realm.executeTransaction {
            body.invoke(follower)
            realm.copyToRealm(follower)
        }
    }

    override fun getAllFollowers(): List<Follower> = realm.where(Follower::class.java).findAll()

    override fun saveTweetInfoList(tweetInfoList: List<TweetInfo>) {
        realm.executeTransaction { realm.copyToRealm(tweetInfoList) }
    }

    override fun getAllTweetInfo(): List<TweetInfo> = realm.where(TweetInfo::class.java).findAll()

    override fun savePrivateMessages(privateMessages: List<PrivateMessage>) {
        realm.executeTransaction { realm.copyToRealm(privateMessages) }
    }

    override fun saveUsersFollowed(userFollowed: List<UserFollowed>) {
        realm.executeTransaction { realm.copyToRealm(userFollowed) }
    }

    override fun saveMentions(mentions: List<Mention>) {
        realm.executeTransaction { realm.copyToRealm(mentions) }
    }

    override fun getAllMentions(): List<Mention> = realm.where(Mention::class.java)
            .findAllSorted("timestamp")

    override fun saveFollowers(followers: List<Follower>) {
        realm.executeTransaction { realm.copyToRealm(followers) }
    }

    override fun clear() {
        realm.executeTransaction {
            realm.delete(Follower::class.java)
            realm.delete(Mention::class.java)
            realm.delete(UserFollowed::class.java)
            realm.delete(TweetInfo::class.java)
            realm.delete(UserId::class.java)
            realm.delete(Notification::class.java)
            realm.delete(PrivateMessage::class.java)
        }
    }

    override fun close() {
        realm.close()
    }
}