package com.andreapivetta.blu

import android.app.Application
import com.andreapivetta.blu.data.twitter.TwitterUtils

/**
 * Created by andrea on 18/05/16.
 */
open class BlumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TwitterUtils.init(applicationContext)
    }

}