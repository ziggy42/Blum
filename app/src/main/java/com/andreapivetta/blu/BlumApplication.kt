package com.andreapivetta.blu

import android.app.Application
import com.andreapivetta.blu.twitter.TwitterUtils
import com.facebook.stetho.Stetho
import timber.log.Timber

/**
 * Created by andrea on 18/05/16.
 */
class BlumApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        TwitterUtils.init(applicationContext)
        // TODO move me to debug version only
        Timber.plant(Timber.DebugTree());

        if(BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

}