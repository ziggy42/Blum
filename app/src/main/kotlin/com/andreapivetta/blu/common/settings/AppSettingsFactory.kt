package com.andreapivetta.blu.common.settings

import android.content.Context

/**
 * Created by andrea on 18/09/16.
 */
object AppSettingsFactory {

    fun getAppSettings(context: Context) = SharedPreferencesAppSettings(context)

}