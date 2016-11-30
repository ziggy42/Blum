package com.andreapivetta.blu

import android.content.Context
import android.preference.PreferenceManager
import timber.log.Timber

/**
 * Created by andrea on 30/11/16.
 * Blum 5.x differs too much from Blum 4.x
 * This class erases all stuff from Blum 4.x to force the user to re-login
 */
object Migration {

    fun migrate(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        if (sharedPreferences.getBoolean("logged", false)) {
            sharedPreferences.edit().clear().apply()
            context.deleteDatabase("blumdb")
            Timber.i("App is now fresh and clean!")
        }
    }

}