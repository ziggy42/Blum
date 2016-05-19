package com.andreapivetta.blu.data

import com.andreapivetta.blu.twitter.TwitterUtils
import rx.Single
import timber.log.Timber
import twitter4j.Paging
import twitter4j.Status
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by andrea on 18/05/16.
 */
object DataManager {

    fun getHomeTimeline(paging: Paging): Single<MutableList<Status>> =
            Single.from(object : Future<MutableList<Status>> {
                override fun get(): MutableList<Status>? {
                    return TwitterUtils.getTwitter().getHomeTimeline(paging)
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
            });

}