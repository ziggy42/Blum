package com.andreapivetta.blu.data.jobs

import com.evernote.android.job.JobCreator

/**
 * Created by andrea on 17/09/16.
 */
class NotificationsJobCreator : JobCreator {

    override fun create(tag: String?) = when (tag) {
        NotificationsJob.TAG -> NotificationsJob()
        else -> throw RuntimeException("No such job $tag")
    }
}