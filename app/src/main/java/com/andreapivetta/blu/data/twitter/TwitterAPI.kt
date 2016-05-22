package com.andreapivetta.blu.data.twitter

import rx.Single
import twitter4j.Paging
import twitter4j.Status
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by andrea on 18/05/16.
 */
object TwitterAPI {

    fun getHomeTimeline(paging: Paging): Single<MutableList<Status>> =
            Single.from(object : Future<MutableList<Status>> {
                override fun get(): MutableList<Status>? =
                        TwitterUtils.getTwitter().getHomeTimeline(paging)

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

    fun refreshTimeLine(paging: Paging): Single<MutableList<Status>> = getHomeTimeline(paging)

    fun updateTwitterStatus(tweet: String?): Single<Status> =
            Single.from(object : Future<Status> {
                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun get(): Status? = TwitterUtils.getTwitter().updateStatus(tweet)

                override fun get(timeout: Long, unit: TimeUnit?): Status? {
                    throw UnsupportedOperationException()
                }

                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    fun favorite(status: Status): Single<Status> =
            Single.from(object : Future<Status> {
                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun get(): Status? = TwitterUtils.getTwitter().createFavorite(status.id)

                override fun get(timeout: Long, unit: TimeUnit?): Status? {
                    throw UnsupportedOperationException()
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    fun unfavorite(status: Status): Single<Status> =
            Single.from(object : Future<Status> {
                override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun isCancelled(): Boolean {
                    throw UnsupportedOperationException()
                }

                override fun get(): Status? = TwitterUtils.getTwitter().destroyFavorite(status.id)

                override fun get(timeout: Long, unit: TimeUnit?): Status? {
                    throw UnsupportedOperationException()
                }

                override fun isDone(): Boolean {
                    throw UnsupportedOperationException()
                }
            })

    fun retweet(status: Status): Single<Status> =
            Single.from(object : Future<Status> {
                override fun get(timeout: Long, unit: TimeUnit?): Status? {
                    throw UnsupportedOperationException()
                }

                override fun get(): Status? = TwitterUtils.getTwitter().retweetStatus(status.id)

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
}

