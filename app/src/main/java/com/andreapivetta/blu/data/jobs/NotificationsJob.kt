package com.andreapivetta.blu.data.jobs

import com.andreapivetta.blu.BuildConfig
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.data.twitter.Twitter
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit

/**
 * Created by andrea on 17/09/16.
 */
class NotificationsJob : Job() {

    companion object {
        const val TAG = "notifications_job"

        fun scheduleJob() {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(BuildConfig.INTERVAL))
                    .setPersisted(true)
                    .setRequiredNetworkType(JobRequest.NetworkType.UNMETERED)
                    .build()
                    .schedule()
        }
    }

    override fun onRunJob(params: Params?): Job.Result {
        val storage = AppStorageFactory.getAppStorage()
        val dispatcher = NotificationDispatcher(context, storage)

        NotificationsDownloader.checkNotifications(AppSettingsFactory.getAppSettings(context),
                storage, Twitter.getInstance(), dispatcher)

        return Result.SUCCESS
    }
}