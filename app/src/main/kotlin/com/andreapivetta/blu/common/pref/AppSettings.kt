package com.andreapivetta.blu.common.pref

import twitter4j.auth.AccessToken

/**
 * Created by andrea on 15/05/16.
 */
interface AppSettings {

    fun isUserLoggedIn(): Boolean

    fun saveAccessToken(accessToken: AccessToken)

    fun getAccessToken(): AccessToken

    fun getLoggedUserId(): Long

    fun saveTheme(theme: String)

    fun getTheme(): String

    fun clear()

}