package com.andreapivetta.blu.data.jobs

import android.support.test.runner.AndroidJUnit4
import com.andreapivetta.blu.BuildConfig
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

/**
 * Created by andrea on 29/11/16.
 */
@RunWith(AndroidJUnit4::class)
class NotificationsDataProviderTest {

    val TWEET_ID_1 = 775039880598552576
    val twitter: Twitter = TwitterFactory(ConfigurationBuilder()
            .setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_KEY)
            .setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_SECRET).build())
            .getInstance(AccessToken(BuildConfig.TEST_TOKEN, BuildConfig.TEST_SECRET))

    @Test
    fun retrieveTweetInfo() {
        val info = NotificationsDataProvider.retrieveTweetInfo(twitter)
        assertTrue(info.isNotEmpty())
        assertEquals(100, info.size)
    }

    @Test
    fun retrieveMentions() {
        val mentions = NotificationsDataProvider.retrieveMentions(twitter)
        assertTrue(mentions.isNotEmpty())
        assertTrue(mentions.size <= 200)
    }

    @Test
    fun retrieveFollowers() {
        val followers = NotificationsDataProvider.retrieveFollowers(twitter)
        assertTrue(followers.isNotEmpty())
    }

    @Test
    fun retrieveDirectMessages() {
        val directMessages = NotificationsDataProvider.retrieveDirectMessages(twitter)
        assertTrue(directMessages.isNotEmpty())
    }

    @Test
    fun safeRetrieveFollowedUsers() {
        val users = NotificationsDataProvider.safeRetrieveFollowedUsers(twitter)
        assertNotNull(users)
        assertTrue(users.isNotEmpty())

        NotificationsDataProvider.safeRetrieveFollowedUsers(twitter, {
            assertNotNull(it)
            assertTrue(it.isNotEmpty())
        })
    }

    @Test
    fun getFavoriters() {
        val users = NotificationsDataProvider.getFavoriters(TWEET_ID_1)
        assertTrue(users != null)
        assertEquals(2, users?.size)
    }

    @Test
    fun getRetweeters() {
        val users = NotificationsDataProvider.getRetweeters(TWEET_ID_1)
        assertTrue(users != null)
        assertEquals(1, users?.size)
    }
}