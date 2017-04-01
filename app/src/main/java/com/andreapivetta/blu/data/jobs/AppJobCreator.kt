package com.andreapivetta.blu.data.jobs

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

/**
 * Created by andrea on 17/09/16.
 */
class AppJobCreator : JobCreator {

    companion object {
        fun scheduleJobs() {
            NotificationsJob.scheduleJob()
            UsersFollowedJob.scheduleJob()
        }
    }

    override fun create(tag: String?): Job = when (tag) {
        NotificationsJob.TAG -> NotificationsJob()
        UsersFollowedJob.TAG -> UsersFollowedJob()
        else -> throw RuntimeException("No such job $tag")
    }
}