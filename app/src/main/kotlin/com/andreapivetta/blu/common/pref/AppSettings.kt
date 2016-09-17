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

    fun setUserDataDownloaded(downloaded: Boolean)

    fun isUserDataDownloaded(): Boolean

    fun setUserFollowedAvailable(userFollowedAvailable: Boolean)

    fun isUserFollowedAvailable(): Boolean

    fun setNotifyFavRet(notify: Boolean)

    fun isNotifyFavRet(): Boolean

    fun setNotifyDirectMessages(notify: Boolean)

    fun isNotifyDirectMessages(): Boolean

    fun setNotifyFollowers(notify: Boolean)

    fun isNotifyFollowers(): Boolean

    fun setNotifyMentions(notify: Boolean)

    fun isNotifyMentions(): Boolean

    fun clear()

}