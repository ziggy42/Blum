package com.andreapivetta.blu.common.pref

import android.content.Context
import android.preference.PreferenceManager
import twitter4j.auth.AccessToken

/**
 * Created by andrea on 15/05/16.
 */
object AppSettingsImpl : AppSettings {

    private var context: Context? = null

    private val KEY_LOGIN = "logged"
    private val KEY_LOGGED_USER = "user"
    private val KEY_OAUTH_TOKEN = "oauth_token"
    private val KEY_OAUTH_TOKEN_SECRET = "oauth_token_secret"
    private val KEY_THEMES = "themes"
    private val KEY_REALM_POPULATED = "realm_populated"
    private val KEY_USER_FOLLOWED_AVAILABLE = "user_followed_available"
    private val KEY_NOTIFY_FAV_RET = "fav_ret"
    private val KEY_NOTIFY_DIRECT_MESSAGES = "dms"
    private val KEY_NOTIFY_FOLLOWERS = "followers"
    private val KEY_NOTIFY_MENTIONS = "mentions"

    fun init(context: Context) {
        this.context = context
    }

    override fun isUserLoggedIn() = PreferenceManager
            .getDefaultSharedPreferences(context).getBoolean(KEY_LOGIN, false)

    override fun saveAccessToken(accessToken: AccessToken) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_OAUTH_TOKEN, accessToken.token)
                .putString(KEY_OAUTH_TOKEN_SECRET, accessToken.tokenSecret)
                .putBoolean(KEY_LOGIN, true)
                .putLong(KEY_LOGGED_USER, accessToken.userId)
                .apply()
    }

    override fun getAccessToken(): AccessToken {
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return AccessToken(mSharedPreferences.getString(KEY_OAUTH_TOKEN, ""),
                mSharedPreferences.getString(KEY_OAUTH_TOKEN_SECRET, ""))
    }

    override fun getLoggedUserId() =
            PreferenceManager.getDefaultSharedPreferences(context).getLong(KEY_LOGGED_USER, 0L)

    override fun saveTheme(theme: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_THEMES, theme).apply()
    }

    override fun getTheme(): String = PreferenceManager
            .getDefaultSharedPreferences(context).getString(KEY_THEMES, "B")

    override fun setUserDataDownloaded(downloaded: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(KEY_REALM_POPULATED, downloaded).apply()
    }

    override fun isUserDataDownloaded(): Boolean = PreferenceManager.
            getDefaultSharedPreferences(context).getBoolean(KEY_REALM_POPULATED, false)

    override fun setUserFollowedAvailable(userFollowedAvailable: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(KEY_USER_FOLLOWED_AVAILABLE, userFollowedAvailable).apply()
    }

    override fun isUserFollowedAvailable(): Boolean = PreferenceManager.
            getDefaultSharedPreferences(context).getBoolean(KEY_USER_FOLLOWED_AVAILABLE, false)

    override fun setNotifyFavRet(notify: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(KEY_NOTIFY_FAV_RET, notify).apply()
    }

    override fun isNotifyFavRet(): Boolean = PreferenceManager.
            getDefaultSharedPreferences(context).getBoolean(KEY_NOTIFY_FAV_RET, false)

    override fun setNotifyDirectMessages(notify: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(KEY_NOTIFY_DIRECT_MESSAGES, notify).apply()
    }

    override fun isNotifyDirectMessages(): Boolean = PreferenceManager.
            getDefaultSharedPreferences(context).getBoolean(KEY_NOTIFY_DIRECT_MESSAGES, false)

    override fun setNotifyFollowers(notify: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(KEY_NOTIFY_FOLLOWERS, notify).apply()
    }

    override fun isNotifyFollowers(): Boolean = PreferenceManager.
            getDefaultSharedPreferences(context).getBoolean(KEY_NOTIFY_FOLLOWERS, false)

    override fun setNotifyMentions(notify: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(KEY_NOTIFY_MENTIONS, notify).apply()
    }

    override fun isNotifyMentions(): Boolean = PreferenceManager.
            getDefaultSharedPreferences(context).getBoolean(KEY_NOTIFY_MENTIONS, false)

    override fun clear() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
    }

}