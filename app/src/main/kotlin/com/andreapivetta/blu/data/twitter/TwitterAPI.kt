package com.andreapivetta.blu.data.twitter

import rx.Observable
import rx.Single
import timber.log.Timber
import twitter4j.*
import java.io.File
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by andrea on 18/05/16.
 */
object TwitterAPI {

    fun getHomeTimeline(paging: Paging): Single<MutableList<Status>> =
            Single.from(object : Future<MutableList<Status>> {
                override fun get() =
                        try {
                            TwitterUtils.getTwitter().getHomeTimeline(paging)
                        } catch(err: Exception) {
                            Timber.e(err, "Error in getHomeTimeline")
                            null
                        }

                override fun get(timeout: Long, unit: TimeUnit?): MutableList<Status>? {
                    throw UnsupportedOperationException()
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    fun getUserTimeline(userId: Long, paging: Paging): Observable<MutableList<Status>> =
            Observable.from(object : Future<MutableList<Status>> {
                override fun get() =
                        try {
                            TwitterUtils.getTwitter().getUserTimeline(userId, paging)
                        } catch(err: Exception) {
                            Timber.e(err, "Error in getUserTimeline")
                            null
                        }

                override fun get(timeout: Long, unit: TimeUnit?): MutableList<Status>? {
                    throw UnsupportedOperationException()
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    return true
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    fun searchTweets(query: Query): Single<QueryResult> =
            Single.from(object : Future<QueryResult> {
                override fun get(p0: Long, p1: TimeUnit?): QueryResult {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun get() =
                        try {
                            TwitterUtils.getTwitter().search(query)
                        } catch (err: Exception) {
                            Timber.e(err, "Error in searchTweets")
                            null
                        }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun cancel(p0: Boolean): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }
            })

    fun searchUsers(query: String, paging: Paging): Single<MutableList<User>> =
            Single.from(object : Future<MutableList<User>> {
                override fun cancel(p0: Boolean): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun get(p0: Long, p1: TimeUnit?): MutableList<User> {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun get(): MutableList<User>? =
                        try {
                            TwitterUtils.getTwitter().searchUsers(query, paging.page)
                        } catch (err: Exception) {
                            Timber.e(err, "Error in searchTweets")
                            null
                        }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }
            })

    fun refreshTimeLine(paging: Paging): Single<MutableList<Status>> = getHomeTimeline(paging)

    fun refreshUserTimeLine(userId: Long, paging: Paging): Observable<MutableList<Status>> =
            getUserTimeline(userId, paging)

    fun updateStatus(tweet: String?): Single<Status> = Single.from(object : Future<Status> {
        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun get() = try {
            TwitterUtils.getTwitter().updateStatus(tweet)
        } catch (err: Exception) {
            Timber.e(err, "Error in updateStatus")
            null
        }

        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }
    })

    fun updateStatus(tweet: String?, images: List<File>): Single<Status> =
            Single.from(object : Future<Status> {
                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun get() = try {
                    val status = StatusUpdate(tweet)
                    val mediaIds = LongArray(images.size)
                    images.forEachIndexed { i, file ->
                        mediaIds[i] = TwitterUtils.getTwitter()
                                .uploadMedia(images[i]).mediaId
                    }
                    status.setMediaIds(*mediaIds)
                    TwitterUtils.getTwitter().updateStatus(status)
                } catch (err: Exception) {
                    Timber.e(err, "Error in updateStatus")
                    null
                }

                override fun get(timeout: Long, unit: TimeUnit?): Status? {
                    throw UnsupportedOperationException()
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    // TODO bleah
    fun updateStatus(statusUpdate: TweetsQueue.StatusUpdate): Single<Status> {
        if (statusUpdate.reply > 0)
            if (statusUpdate.files.isEmpty())
                return reply(statusUpdate.text, statusUpdate.reply)
            else
                return reply(statusUpdate.text, statusUpdate.reply, statusUpdate.files)
        else
            if (statusUpdate.files.isEmpty())
                return updateStatus(statusUpdate.text)
            else
                return updateStatus(statusUpdate.text, statusUpdate.files)
    }

    fun destroy(statusId: Long): Single<Status> = Single.from(object : Future<Status> {
        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun get() = try {
            TwitterUtils.getTwitter().destroyStatus(statusId)
        } catch (err: Exception) {
            Timber.e(err, "Error in destroy")
            null
        }

        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }
    })

    fun reply(tweet: String?, inReplyToStatusId: Long): Single<Status> =
            Single.from(object : Future<Status> {
                override fun get() = try {
                    val status = StatusUpdate(tweet)
                    status.inReplyToStatusId = inReplyToStatusId
                    TwitterUtils.getTwitter().updateStatus(status)
                } catch (err: Exception) {
                    null
                }

                override fun get(timeout: Long, unit: TimeUnit?): Status? {
                    throw UnsupportedOperationException()
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    fun reply(tweet: String?, inReplyToStatusId: Long, images: List<File>): Single<Status> =
            Single.from(object : Future<Status> {
                override fun get() = try {
                    val status = StatusUpdate(tweet)
                    val mediaIds = LongArray(images.size)
                    images.forEachIndexed { i, file ->
                        mediaIds[i] = TwitterUtils.getTwitter()
                                .uploadMedia(images[i]).mediaId
                    }
                    status.setMediaIds(*mediaIds)
                    status.inReplyToStatusId = inReplyToStatusId
                    TwitterUtils.getTwitter().updateStatus(status)
                } catch (err: Exception) {
                    Timber.e(err, "Error in reply")
                    null
                }

                override fun get(timeout: Long, unit: TimeUnit?): Status? {
                    throw UnsupportedOperationException()
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    fun favorite(statusId: Long): Single<Status> = Single.from(object : Future<Status> {
        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun get() = try {
            TwitterUtils.getTwitter().createFavorite(statusId)
        } catch (err: Exception) {
            Timber.e(err, "Error in favorite")
            null
        }

        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }
    })

    fun unfavorite(statusId: Long): Single<Status> = Single.from(object : Future<Status> {
        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun get() = try {
            TwitterUtils.getTwitter().destroyFavorite(statusId)
        } catch (err: Exception) {
            Timber.e(err, "Error in unfavorite")
            null
        }

        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }
    })

    fun retweet(statusId: Long): Single<Status> = Single.from(object : Future<Status> {
        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun get() = try {
            TwitterUtils.getTwitter().retweetStatus(statusId)
        } catch (err: Exception) {
            Timber.e(err, "Error in retweet")
            null
        }

        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }
    })

    fun unretweet(statusId: Long) = destroy(statusId)

    fun getConversation(statusId: Long): Single<Pair<MutableList<Status>, Int>> =
            Single.from(object : Future<Pair<MutableList<Status>, Int>> {
                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun get(): Pair<MutableList<Status>, Int>? {
                    try {
                        val list = ArrayList<Status>()
                        val status = TwitterUtils.getTwitter().showStatus(statusId)
                        var currentStatus = status
                        var id: Long = currentStatus.inReplyToStatusId
                        while (id != -1L) {
                            currentStatus = TwitterUtils.getTwitter().showStatus(id)
                            list.add(currentStatus)
                            id = currentStatus.inReplyToStatusId
                        }

                        val targetIndex = list.size
                        list.add(status)

                        val result = TwitterUtils.getTwitter()
                                .search(Query("to: ${status.user.screenName}"))
                        result.tweets.forEach {
                            tmpStatus ->
                            if (status.id == tmpStatus.inReplyToStatusId) list.add(tmpStatus)
                        }
                        return Pair(list, targetIndex)
                    } catch(err: Exception) {
                        Timber.e(err, "Error in getConversation")
                        return null
                    }
                }

                override fun get(timeout: Long, unit: TimeUnit?): Pair<MutableList<Status>, Int>? {
                    throw UnsupportedOperationException()
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    fun showUser(userId: Long): Single<User> =
            Single.from(object : Future<User> {
                override fun isDone(): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun get(): User? = try {
                    TwitterUtils.getTwitter().showUser(userId)
                } catch (err: Exception) {
                    Timber.e(err, "Error in getting a User")
                    null
                }

                override fun get(timeout: Long, unit: TimeUnit?): User {
                    throw UnsupportedOperationException("not implemented")
                }
            })

    fun sendPrivateMessage(text: String, userId: Long): Single<DirectMessage> =
            Single.from(object : Future<DirectMessage> {
                override fun get(timeout: Long, unit: TimeUnit?): DirectMessage {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun get(): DirectMessage? = try {
                    TwitterUtils.getTwitter().sendDirectMessage(userId, text)
                } catch (err: Exception) {
                    Timber.e(err, "Error in getting a User")
                    null
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException("not implemented")
                }
            })
}