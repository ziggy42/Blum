package com.andreapivetta.blu

import android.app.Application
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.data.jobs.AppJobCreator
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.data.storage.NotificationPrimaryKeyGenerator
import com.andreapivetta.blu.data.twitter.TweetsQueue
import com.andreapivetta.blu.data.twitter.Twitter
import com.evernote.android.job.JobManager
import io.realm.Realm

/**
 * Created by andrea on 18/05/16.
 */
open class BlumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Twitter.init(AppSettingsFactory.getAppSettings(this))
        JobManager.create(this).addJobCreator(AppJobCreator())
        Realm.init(this)
        TweetsQueue.init(this)
        NotificationPrimaryKeyGenerator.init(AppStorageFactory.getAppStorage())
    }

}