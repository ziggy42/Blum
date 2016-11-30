package com.andreapivetta.blu.data.jobs

import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.data.twitter.Twitter
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Created by andrea on 29/11/16.
 */
class UsersFollowedJob : Job() {

    companion object {
        const val TAG = "users_job"

        fun scheduleJob() {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.DAYS.toMillis(2))
                    .setPersisted(true)
                    .setRequiredNetworkType(JobRequest.NetworkType.UNMETERED)
                    .build()
                    .schedule()
        }
    }

    override fun onRunJob(params: Params?): Result = checkUserFollowed()

    fun checkUserFollowed(): Result {
        val settings = AppSettingsFactory.getAppSettings(context)
        val storage = AppStorageFactory.getAppStorage()
        val twitter = Twitter.getInstance()

        Timber.d("Checking for new users followed...")

        val users = NotificationsDataProvider.safeRetrieveFollowedUsers(twitter)
        storage.updateUsersFollowed(users)
        if (users.isEmpty())
            settings.setUserFollowedAvailable(false)

        storage.close()
        Timber.d("Done")

        return Result.SUCCESS
    }
}