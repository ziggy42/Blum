package com.andreapivetta.blu

import android.app.Application
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.data.jobs.NotificationsJobCreator
import com.andreapivetta.blu.data.storage.NotificationPrimaryKeyGenerator
import com.andreapivetta.blu.data.twitter.TwitterUtils
import com.evernote.android.job.JobManager

/**
 * Created by andrea on 18/05/16.
 */
open class BlumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TwitterUtils.init(AppSettingsFactory.getAppSettings(this))
        NotificationPrimaryKeyGenerator.init(this)
        JobManager.create(this).addJobCreator(NotificationsJobCreator())
    }

}