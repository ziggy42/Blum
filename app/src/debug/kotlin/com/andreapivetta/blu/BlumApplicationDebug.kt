package com.andreapivetta.blu

import android.app.Application
import com.andreapivetta.blu.data.twitter.TwitterUtils
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

/**
 * Created by andrea on 18/05/16.
 */
class BlumApplicationDebug : BlumApplication() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree());
        LeakCanary.install(this)
        Stetho.initializeWithDefaults(this)
        Timber.i("Stuff initialized")
    }

}