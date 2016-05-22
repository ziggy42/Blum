package com.andreapivetta.blu.data.twitter

import android.content.Context
import android.preference.PreferenceManager
import com.andreapivetta.blu.BuildConfig
import com.andreapivetta.blu.R
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

/**
 * Created by andrea on 15/05/16.
 */
object TwitterUtils {

    private var accessToken: AccessToken? = null
    private var factory: TwitterFactory? = null

    private fun getFactory(): TwitterFactory {
        if (factory == null) {
            val builder = ConfigurationBuilder()
            builder.setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY).setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET)
            factory = TwitterFactory(builder.build())
        }

        return factory as TwitterFactory
    }

    fun getTwitter(): Twitter = getFactory().getInstance(accessToken)

    fun nullTwitter() {
        factory = null
        accessToken = null
    }

    fun init(context: Context?) {
        // TODO Use Settings
        val mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        accessToken = AccessToken(mSharedPreferences.getString(context?.getString(R.string.pref_oauth_token), ""),
                mSharedPreferences.getString(context?.getString(R.string.pref_oauth_token_secret), ""))
    }
}
