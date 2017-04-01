package com.andreapivetta.blu.common.utils

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by andrea on 20/09/16.
 */
class UtilsTest {

    private val testUrl = "http://the-toast.net/wp-content/uploads/2015/07/Futurama_fry_looking_squint2.jpg"

    @Test
    fun getBitmapFromURL() {
        val bitmap = Utils.getBitmapFromURL(testUrl)
        assertNotNull(bitmap)
        assertTrue(bitmap?.byteCount ?: 0 > 0)
    }

}