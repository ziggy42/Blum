package com.andreapivetta.blu.data.twitter

import rx.Single
import twitter4j.Paging
import twitter4j.Query
import twitter4j.Status
import twitter4j.StatusUpdate
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by andrea on 18/05/16.
 */
object TwitterAPI {

    fun getHomeTimeline(paging: Paging) = Single.from(object : Future<MutableList<Status>> {
        override fun get() = TwitterUtils.getTwitter().getHomeTimeline(paging)

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

    fun refreshTimeLine(paging: Paging) = getHomeTimeline(paging)

    fun updateTwitterStatus(tweet: String?) = Single.from(object : Future<Status> {
        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun get() = TwitterUtils.getTwitter().updateStatus(tweet)

        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }
    })

    fun reply(tweet: String?, inReplyToStatusId: Long) = Single.from(object : Future<Status> {
        override fun get(): Status? {
            val status = StatusUpdate(tweet)
            status.inReplyToStatusId = inReplyToStatusId
            return TwitterUtils.getTwitter().updateStatus(status)
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

    fun favorite(status: Status) = Single.from(object : Future<Status> {
        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun get() = TwitterUtils.getTwitter().createFavorite(status.id)

        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }
    })

    fun unfavorite(status: Status) = Single.from(object : Future<Status> {
        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }

        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun get() = TwitterUtils.getTwitter().destroyFavorite(status.id)

        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }
    })

    fun retweet(status: Status) = Single.from(object : Future<Status> {
        override fun get(timeout: Long, unit: TimeUnit?): Status? {
            throw UnsupportedOperationException()
        }

        override fun get() = TwitterUtils.getTwitter().retweetStatus(status.id)

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

    fun getConversation(statusId: Long) = Single.from(object : Future<Pair<MutableList<Status>, Int>> {
        override fun isCancelled(): Boolean {
            throw UnsupportedOperationException()
        }

        override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
            throw UnsupportedOperationException()
        }

        override fun get(): Pair<MutableList<Status>, Int>? {
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

            val result = TwitterUtils.getTwitter().search(Query("to:" + status.user.screenName))
            result.tweets.forEach {
                tmpStatus ->
                if (status.id == tmpStatus.inReplyToStatusId) list.add(tmpStatus)
            }
            return Pair(list, targetIndex)
        }

        override fun get(timeout: Long, unit: TimeUnit?): Pair<MutableList<Status>, Int>? {
            throw UnsupportedOperationException()
        }

        override fun isDone(): Boolean {
            throw UnsupportedOperationException()
        }
    })

}

