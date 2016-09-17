package com.andreapivetta.blu.data.jobs

import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit

/**
 * Created by andrea on 17/09/16.
 */
class NotificationsJob : Job() {

    companion object {
        val TAG = "notifications_job"

        fun scheduleJob() {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(2))
                    .setPersisted(true)
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .build()
                    .schedule()
        }
    }

    override fun onRunJob(params: Params?): Result {


        return Result.SUCCESS
    }
}