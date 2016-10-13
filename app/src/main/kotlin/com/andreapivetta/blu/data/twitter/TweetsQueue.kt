package com.andreapivetta.blu.data.twitter

import android.content.Context
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.notifications.AppNotifications
import com.andreapivetta.blu.common.notifications.AppNotificationsFactory
import rx.SingleSubscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Status
import java.io.File
import java.util.*

/**
 * Created by andrea on 13/10/16.
 */
object TweetsQueue {

    data class StatusUpdate(val text: String, val files: List<File>, val reply: Long = -1) {
        companion object {
            fun valueOf(text: String) = StatusUpdate(text, listOf())

            fun valueOf(text: String, reply: Long) = StatusUpdate(text, listOf(), reply)

            fun valueOf(text: String, files: List<File>) = StatusUpdate(text, files)

            fun valueOf(text: String, files: List<File>, reply: Long) =
                    StatusUpdate(text, files, reply)
        }
    }

    val queue: Queue<StatusUpdate> = LinkedList<StatusUpdate>()
    var appNotifications: AppNotifications? = null
    var context: Context? = null
    var sending = false

    fun init(context: Context) {
        this.context = context
        this.appNotifications = AppNotificationsFactory.getAppNotifications(context)
    }

    fun add(update: StatusUpdate) {
        queue.add(update)
        tick()
    }

    @Synchronized private fun tick() {
        if (!sending && queue.isNotEmpty()) {
            sending = true
            val id = appNotifications?.sendLongRunning(
                    context!!.getString(R.string.sending_tweet_title),
                    context!!.getString(R.string.sending_tweet_content), R.drawable.ic_publish)
            TwitterAPI.updateStatus(queue.poll())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : SingleSubscriber<Status>() {
                        override fun onSuccess(value: Status?) {
                            sending = false
                            appNotifications?.stopLongRunning(id!!)
                            tick()
                        }

                        override fun onError(error: Throwable?) {
                            Timber.e(error?.message)
                            sending = false
                            appNotifications?.stopLongRunning(id!!)
                            tick()
                        }
                    })
        }
    }

}