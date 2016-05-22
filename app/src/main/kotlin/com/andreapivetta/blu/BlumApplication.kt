package com.andreapivetta.blu

import android.app.Application
import com.andreapivetta.blu.data.twitter.TwitterUtils
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

/**
 * Created by andrea on 18/05/16.
 */
class BlumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TwitterUtils.init(applicationContext)
        Timber.plant(Timber.DebugTree());
        if (BuildConfig.DEBUG) LeakCanary.install(this)
    }

}