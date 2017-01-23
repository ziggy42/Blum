package com.andreapivetta.blu.data.twitter

import android.content.Context
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.notifications.AppNotifications
import com.andreapivetta.blu.common.notifications.AppNotificationsFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.io.InputStream
import java.util.*

/**
 * Created by andrea on 13/10/16.
 */
object TweetsQueue {

    data class StatusUpdate(val text: String, val streams: List<InputStream>, val reply: Long = -1) {
        companion object {
            fun valueOf(text: String) = StatusUpdate(text, listOf())

            fun valueOf(text: String, reply: Long) = StatusUpdate(text, listOf(), reply)

            fun valueOf(text: String, files: List<InputStream>) = StatusUpdate(text, files)

            fun valueOf(text: String, files: List<InputStream>, reply: Long) =
                    StatusUpdate(text, files, reply)
        }
    }

    private val queue: Queue<StatusUpdate> = LinkedList<StatusUpdate>()
    private lateinit var appNotifications: AppNotifications
    private lateinit var context: Context
    private var sending = false

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
            val id = appNotifications.sendLongRunning(
                    context.getString(R.string.sending_tweet_title),
                    context.getString(R.string.sending_tweet_content), R.drawable.ic_publish)
            TwitterAPI.updateStatus(queue.poll())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        sending = false
                        appNotifications.stopLongRunning(id)
                        tick()
                    }, {
                        Timber.e(it)
                        sending = false
                        appNotifications.stopLongRunning(id)
                        tick()
                    })
        }
    }

}