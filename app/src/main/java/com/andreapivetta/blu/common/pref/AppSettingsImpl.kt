package com.andreapivetta.blu.common.pref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.andreapivetta.blu.R

/**
 * Created by andrea on 15/05/16.
 */
class AppSettingsImpl(context: Context) : AppSettings {

    val context = context
    val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun isUserLoggedIn() =
            pref.getBoolean(context.getString(R.string.pref_key_login), false)

    override fun setUserLoggedIn(logged: Boolean) {
        pref.edit().putBoolean(context.getString(R.string.pref_key_logged_user), logged).apply()
    }

}