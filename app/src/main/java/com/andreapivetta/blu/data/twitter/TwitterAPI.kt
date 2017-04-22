package com.andreapivetta.blu.data.twitter

import io.reactivex.Observable
import io.reactivex.Single
import twitter4j.*
import java.io.InputStream

/**
 * Created by andrea on 18/05/16.
 */
object TwitterAPI {

    private val twitter = Twitter.getInstance()

    fun getHomeTimeline(paging: Paging): Single<MutableList<Status>> =
            Single.fromCallable { twitter.getHomeTimeline(paging) }

    fun getUserTimeline(userId: Long, paging: Paging): Observable<MutableList<Status>> =
            Observable.fromCallable { twitter.getUserTimeline(userId, paging) }

    fun searchTweets(query: Query): Single<QueryResult> =
            Single.fromCallable { twitter.search(query) }

    fun searchUsers(query: String, paging: Paging): Single<MutableList<User>> =
            Single.fromCallable { twitter.searchUsers(query, paging.page) }

    fun refreshTimeLine(paging: Paging): Single<MutableList<Status>> = getHomeTimeline(paging)

    fun refreshUserTimeLine(userId: Long, paging: Paging): Observable<MutableList<Status>> =
            getUserTimeline(userId, paging)

    fun updateStatus(tweet: String?): Single<Status> =
            Single.fromCallable { twitter.updateStatus(tweet) }

    fun updateStatus(tweet: String?, images: List<InputStream>): Single<Status> = Single.fromCallable {
        val status = StatusUpdate(tweet)
        val mediaIds = LongArray(images.size)
        images.forEachIndexed { i, stream ->
            mediaIds[i] = twitter.uploadMedia("", stream).mediaId
            stream.close()
        }
        status.setMediaIds(*mediaIds)
        twitter.updateStatus(status)
    }

    fun updateStatus(statusUpdate: TweetsQueue.StatusUpdate): Single<Status> {
        if (statusUpdate.reply > 0)
            if (statusUpdate.streams.isEmpty())
                return reply(statusUpdate.text, statusUpdate.reply)
            else
                return reply(statusUpdate.text, statusUpdate.reply, statusUpdate.streams)
        else
            if (statusUpdate.streams.isEmpty())
                return updateStatus(statusUpdate.text)
            else
                return updateStatus(statusUpdate.text, statusUpdate.streams)
    }

    fun destroy(statusId: Long): Single<Status> =
            Single.fromCallable { twitter.destroyStatus(statusId) }

    fun reply(tweet: String?, inReplyToStatusId: Long): Single<Status> = Single.fromCallable {
        val status = StatusUpdate(tweet)
        status.inReplyToStatusId = inReplyToStatusId
        twitter.updateStatus(status)
    }

    fun reply(tweet: String?, inReplyToStatusId: Long, images: List<InputStream>): Single<Status> =
            Single.fromCallable {
                val status = StatusUpdate(tweet)
                val mediaIds = LongArray(images.size)
                images.forEachIndexed { i, stream ->
                    mediaIds[i] = twitter.uploadMedia("", stream).mediaId
                    stream.close()
                }
                status.setMediaIds(*mediaIds)
                status.inReplyToStatusId = inReplyToStatusId
                twitter.updateStatus(status)
            }

    fun favorite(statusId: Long): Single<Status> =
            Single.fromCallable { twitter.createFavorite(statusId) }

    fun unfavorite(statusId: Long): Single<Status> =
            Single.fromCallable { twitter.destroyFavorite(statusId) }

    fun retweet(statusId: Long): Single<Status> =
            Single.fromCallable { twitter.retweetStatus(statusId) }

    fun unretweet(statusId: Long) = destroy(statusId)

    fun getConversation(statusId: Long): Single<Pair<MutableList<Status>, Int>> =
            Single.fromCallable {
                val list = ArrayList<Status>()
                val status = twitter.showStatus(statusId)
                var currentStatus = status
                var id: Long = currentStatus.inReplyToStatusId
                while (id != -1L) {
                    currentStatus = twitter.showStatus(id)
                    list.add(currentStatus)
                    id = currentStatus.inReplyToStatusId
                }

                val targetIndex = list.size
                list.add(status)

                val result = twitter.search(Query("to: ${status.user.screenName}"))
                result.tweets.forEach {
                    tmpStatus ->
                    if (status.id == tmpStatus.inReplyToStatusId) list.add(tmpStatus)
                }
                Pair(list, targetIndex)
            }

    fun showUser(userId: Long): Single<User> = Single.fromCallable { twitter.showUser(userId) }

    fun showUser(userScreenName: String): Single<User> =
            Single.fromCallable { twitter.showUser(userScreenName) }

    fun sendPrivateMessage(text: String, userId: Long): Single<DirectMessage> =
            Single.fromCallable { twitter.sendDirectMessage(userId, text) }

    fun getFriendship(firstUserId: Long, secondUserId: Long): Single<Relationship> =
            Single.fromCallable { twitter.showFriendship(firstUserId, secondUserId) }

    fun follow(id: Long): Single<User> = Single.fromCallable { twitter.createFriendship(id) }

    fun unfollow(id: Long): Single<User> = Single.fromCallable { twitter.destroyFriendship(id) }
}