package com.andreapivetta.blu.data.twitter

import com.andreapivetta.blu.BuildConfig
import com.andreapivetta.blu.common.settings.AppSettings
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

/**
 * Created by andrea on 15/05/16.
 */
object Twitter {

    private var accessToken: AccessToken? = null
    private var factory: TwitterFactory? = null

    private fun getFactory(): TwitterFactory {
        if (factory == null)
            factory = TwitterFactory(ConfigurationBuilder()
                    .setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY)
                    .setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET).build())
        return factory as TwitterFactory
    }

    fun getInstance(): Twitter = getFactory().getInstance(accessToken)

    fun nullTwitter() {
        factory = null
        accessToken = null
    }

    fun init(appSettings: AppSettings) {
        accessToken = appSettings.getAccessToken()
    }

}
