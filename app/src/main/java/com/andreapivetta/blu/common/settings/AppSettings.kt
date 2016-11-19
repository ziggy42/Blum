package com.andreapivetta.blu.common.settings

import twitter4j.auth.AccessToken

/**
 * Created by andrea on 15/05/16.
 */
interface AppSettings {

    fun isUserLoggedIn(): Boolean

    fun saveAccessToken(accessToken: AccessToken)

    fun getAccessToken(): AccessToken

    fun getLoggedUserId(): Long

    fun getLoggedUserScreenName(): String

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

    fun setDirectMessagesDownloaded(boolean: Boolean)

    fun isDirectMessagesDownloaded(): Boolean

    fun setFollowersDownloaded(boolean: Boolean)

    fun isFollowersDownloaded(): Boolean

    fun setMentionsDownloaded(boolean: Boolean)

    fun isMentionsDownloaded(): Boolean

    fun setFavRetDownloaded(boolean: Boolean)

    fun isFavRetDownloaded(): Boolean

    fun clear()

}