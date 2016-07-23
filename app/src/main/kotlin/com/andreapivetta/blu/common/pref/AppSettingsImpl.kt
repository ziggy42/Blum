package com.andreapivetta.blu.common.pref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by andrea on 15/05/16.
 */
class AppSettingsImpl(val context: Context) : AppSettings {

    private val KEY_LOGIN = "logged"

    val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun isUserLoggedIn() = pref.getBoolean(KEY_LOGIN, false)

}