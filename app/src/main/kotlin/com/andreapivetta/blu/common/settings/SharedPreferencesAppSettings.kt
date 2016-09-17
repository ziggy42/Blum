package com.andreapivetta.blu.common.settings

import android.content.Context
import android.preference.PreferenceManager
import twitter4j.auth.AccessToken

/**
 * Created by andrea on 15/05/16.
 */
class SharedPreferencesAppSettings(context: Context) : AppSettings {

    companion object {
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
        private val KEY_DOWNLOADED_FAV_RET = "downloaded_fav_ret"
        private val KEY_DOWNLOADED_DIRECT_MESSAGES = "downloaded_dms"
        private val KEY_DOWNLOADED_FOLLOWERS = "downloaded_followers"
        private val KEY_DOWNLOADED_MENTIONS = "downloaded_mentions"
    }

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun isUserLoggedIn() = sharedPreferences.getBoolean(KEY_LOGIN, false)

    override fun saveAccessToken(accessToken: AccessToken) {
        sharedPreferences.edit()
                .putString(KEY_OAUTH_TOKEN, accessToken.token)
                .putString(KEY_OAUTH_TOKEN_SECRET, accessToken.tokenSecret)
                .putBoolean(KEY_LOGIN, true)
                .putLong(KEY_LOGGED_USER, accessToken.userId)
                .apply()
    }

    override fun getAccessToken(): AccessToken = AccessToken(
            sharedPreferences.getString(KEY_OAUTH_TOKEN, ""),
            sharedPreferences.getString(KEY_OAUTH_TOKEN_SECRET, ""))

    override fun getLoggedUserId() = sharedPreferences.getLong(KEY_LOGGED_USER, 0L)

    override fun saveTheme(theme: String) {
        sharedPreferences.edit().putString(KEY_THEMES, theme).apply()
    }

    override fun getTheme(): String = sharedPreferences.getString(KEY_THEMES, "B")

    override fun setUserDataDownloaded(downloaded: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_REALM_POPULATED, downloaded).apply()
    }

    override fun isUserDataDownloaded(): Boolean = sharedPreferences
            .getBoolean(KEY_REALM_POPULATED, false)

    override fun setUserFollowedAvailable(userFollowedAvailable: Boolean) {
        sharedPreferences.edit()
                .putBoolean(KEY_USER_FOLLOWED_AVAILABLE, userFollowedAvailable).apply()
    }

    override fun isUserFollowedAvailable(): Boolean = sharedPreferences
            .getBoolean(KEY_USER_FOLLOWED_AVAILABLE, false)

    override fun setNotifyFavRet(notify: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFY_FAV_RET, notify).apply()
    }

    override fun isNotifyFavRet(): Boolean = sharedPreferences.getBoolean(KEY_NOTIFY_FAV_RET, false)

    override fun setNotifyDirectMessages(notify: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFY_DIRECT_MESSAGES, notify).apply()
    }

    override fun isNotifyDirectMessages(): Boolean = sharedPreferences
            .getBoolean(KEY_NOTIFY_DIRECT_MESSAGES, false)

    override fun setNotifyFollowers(notify: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFY_FOLLOWERS, notify).apply()
    }

    override fun isNotifyFollowers(): Boolean = sharedPreferences
            .getBoolean(KEY_NOTIFY_FOLLOWERS, false)

    override fun setNotifyMentions(notify: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFY_MENTIONS, notify).apply()
    }

    override fun isNotifyMentions(): Boolean = sharedPreferences
            .getBoolean(KEY_NOTIFY_MENTIONS, false)

    override fun setDirectMessagesDownloaded(boolean: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DOWNLOADED_DIRECT_MESSAGES, boolean).apply()
    }

    override fun isDirectMessagesDownloaded(): Boolean = sharedPreferences
            .getBoolean(KEY_DOWNLOADED_DIRECT_MESSAGES, false)

    override fun setFollowersDownloaded(boolean: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DOWNLOADED_FOLLOWERS, boolean).apply()
    }

    override fun isFollowersDownloaded(): Boolean = sharedPreferences
            .getBoolean(KEY_DOWNLOADED_FOLLOWERS, false)

    override fun setMentionsDownloaded(boolean: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DOWNLOADED_MENTIONS, boolean).apply()
    }

    override fun isMentionsDownloaded(): Boolean = sharedPreferences
            .getBoolean(KEY_DOWNLOADED_MENTIONS, false)

    override fun setFavRetDownloaded(boolean: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DOWNLOADED_FAV_RET, boolean).apply()
    }

    override fun isFavRetDownloaded(): Boolean = sharedPreferences
            .getBoolean(KEY_DOWNLOADED_FAV_RET, false)

    override fun clear() {
        sharedPreferences.edit().clear().commit()
    }

}