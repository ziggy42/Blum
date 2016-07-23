package com.andreapivetta.blu.common.pref

import android.content.Context
import android.preference.PreferenceManager
import twitter4j.auth.AccessToken

/**
 * Created by andrea on 15/05/16.
 */
object AppSettingsImpl : AppSettings {

    private val KEY_LOGIN = "logged"
    private val KEY_LOGGED_USER = "user"
    private val KEY_OAUTH_TOKEN = "oauth_token"
    private val KEY_OAUTH_TOKEN_SECRET = "oauth_token_secret"
    private val KEY_THEMES = "themes"

    override fun isUserLoggedIn(context: Context) = PreferenceManager
            .getDefaultSharedPreferences(context).getBoolean(KEY_LOGIN, false)

    override fun saveAccessToken(context: Context, accessToken: AccessToken) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_OAUTH_TOKEN, accessToken.token)
                .putString(KEY_OAUTH_TOKEN_SECRET, accessToken.tokenSecret)
                .putBoolean(KEY_LOGIN, true)
                .putLong(KEY_LOGGED_USER, accessToken.userId)
                .apply()
    }

    override fun getAccessToken(context: Context): AccessToken {
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return AccessToken(mSharedPreferences.getString(KEY_OAUTH_TOKEN, ""),
                mSharedPreferences.getString(KEY_OAUTH_TOKEN_SECRET, ""))
    }

    override fun saveTheme(context: Context, theme: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_THEMES, theme).apply()
    }

    override fun getTheme(context: Context): String = PreferenceManager
            .getDefaultSharedPreferences(context).getString(KEY_THEMES, "B")

}