package com.andreapivetta.blu.data.jobs

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by andrea on 29/11/16.
 */
@RunWith(AndroidJUnit4::class)
class NotificationsDataProviderTest {

    val TWEET_ID_1 = 775039880598552576

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