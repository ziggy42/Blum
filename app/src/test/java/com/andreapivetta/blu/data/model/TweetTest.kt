package com.andreapivetta.blu.data.model

import com.andreapivetta.blu.mockobjects.ExtendedMediaEntityImplementation
import com.andreapivetta.blu.mockobjects.StatusImplementation
import com.andreapivetta.blu.mockobjects.UserImplementation
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Created by andrea on 20/07/16.
 */
class TweetTest {

    val mediaEntity1 = ExtendedMediaEntityImplementation()
    val mediaEntity2 = ExtendedMediaEntityImplementation()

    val mockStatus1 = StatusImplementation()
    val mockStatus2 = StatusImplementation()
    val mockStatus3 = StatusImplementation()

    var tweet1: Tweet? = null
    var tweet2: Tweet? = null
    var tweet3: Tweet? = null

    @Before
    fun init() {
        mediaEntity1.mockId = 756222719298002944
        mediaEntity1.mockURL = "https://t.co/rwImyHL5Aj"
        mediaEntity1.mockMediaURL = "http://pbs.twimg.com/media/Cn6kjnrWYAA1qvi.jpg"
        mediaEntity1.mockMediaURLHttps = "https://pbs.twimg.com/media/Cn6kjnrWYAA1qvi.jpg"
        mediaEntity1.mockType = "photo"

        mediaEntity2.mockId = 756222719298002944
        mediaEntity2.mockURL = "https://t.co/rwImyHL5Aj"
        mediaEntity2.mockMediaURL = "http://pbs.twimg.com/media/Cn6kjnrWYAA1qvi.jpg"
        mediaEntity2.mockMediaURLHttps = "https://pbs.twimg.com/media/Cn6kjnrWYAA1qvi.jpg"
        mediaEntity2.mockType = "photo"

        mockStatus1.mockId = 756222722787737600
        mockStatus1.mockText = "https://t.co/rwImyHL5Aj"
        mockStatus1.mockCreatedAt = Calendar.getInstance().time
        mockStatus1.mockExtendedMediaEntities = arrayOf(mediaEntity1)
        mockStatus1.mockUser = UserImplementation()

        mockStatus2.mockId = 756222722787737600
        mockStatus2.mockText = "Hello, World! https://t.co/rwImyHL5Aj"
        mockStatus2.mockCreatedAt = Calendar.getInstance().time
        mockStatus2.mockExtendedMediaEntities = arrayOf(mediaEntity1, mediaEntity2)
        mockStatus2.mockUser = UserImplementation()

        mockStatus3.mockId = 756222722787737600
        mockStatus3.mockText = "Hi @Pivix00#test, how are you #today??"
        mockStatus3.mockCreatedAt = Calendar.getInstance().time
        mockStatus3.mockExtendedMediaEntities = arrayOf()

        mockStatus3.mockUser = UserImplementation()

        tweet1 = Tweet(mockStatus1)
        tweet2 = Tweet(mockStatus2)
        tweet3 = Tweet(mockStatus3)
    }

    @Test
    fun testMedia() {
        assertTrue(tweet1!!.hasSingleImage())
        assertFalse(tweet2!!.hasSingleImage())
        assertTrue(tweet2!!.hasMultipleMedia())
    }

    @Test
    fun testGetTextWithoutMediaURLs() {
        assertTrue(tweet1!!.getTextWithoutMediaURLs().isEmpty())
        assertFalse(tweet2!!.getTextWithoutMediaURLs().isEmpty())
        assertTrue("Hello, World! " == tweet2!!.getTextWithoutMediaURLs())
    }

    @Test
    fun testGetTextAsHtmlString() {
        assertTrue(tweet1!!.getTextAsHtmlString().isEmpty())
        assertFalse(tweet2!!.getTextAsHtmlString().isEmpty())
        assertEquals("Hello, World!", tweet2!!.getTextAsHtmlString())
        assertFalse(tweet3!!.getTextAsHtmlString().isEmpty())
        assertEquals("Hi <a href=\"com.andreapivetta.blu.user://Pivix00\">@Pivix00</a>#test, how " +
                "are you <a href=\"com.andreapivetta.blu.hashtag://today\">#today</a>??",
                tweet3!!.getTextAsHtmlString())
    }

}