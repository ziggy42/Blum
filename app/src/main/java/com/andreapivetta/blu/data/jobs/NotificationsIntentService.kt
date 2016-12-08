package com.andreapivetta.blu.data.jobs

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.data.twitter.Twitter


/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
class NotificationsIntentService : IntentService("NotificationsIntentService") {

    companion object {
        fun startService(context: Context) {
            context.startService(Intent(context, NotificationsIntentService::class.java))
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        val storage = AppStorageFactory.getAppStorage()
        val dispatcher = NotificationDispatcher(this, storage)

        NotificationsDownloader.checkNotifications(AppSettingsFactory.getAppSettings(this),
                storage, Twitter.getInstance(), dispatcher)
    }

}
