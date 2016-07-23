package com.andreapivetta.blu.common.pref

import android.content.Context
import twitter4j.auth.AccessToken

/**
 * Created by andrea on 15/05/16.
 */
interface AppSettings {

    fun isUserLoggedIn(context: Context): Boolean

    fun saveAccessToken(context: Context, accessToken: AccessToken)

    fun getAccessToken(context: Context): AccessToken

    fun saveTheme(context: Context, theme: String)

    fun getTheme(context: Context): String

}