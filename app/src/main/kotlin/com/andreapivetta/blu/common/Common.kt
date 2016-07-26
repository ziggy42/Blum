package com.andreapivetta.blu.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.pref.AppSettingsImpl
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

object Common {

    private val ALWAYS = "always"
    private val WIFI_ONLY = "wifi"

    private val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36"
    private val FAVORITERS_URL = "https://twitter.com/i/activity/favorited_popup?id="
    private val RETWEETERS_URL = "https://twitter.com/i/activity/retweeted_popup?id="

    @Throws(Exception::class)
    fun getFavoriters(tweetID: Long): ArrayList<Long>? {
        return getUsers(tweetID, FAVORITERS_URL)
    }

    @Throws(Exception::class)
    fun getRetweeters(tweetID: Long): ArrayList<Long>? {
        return getUsers(tweetID, RETWEETERS_URL)
    }

    @Throws(Exception::class)
    private fun getUsers(tweetID: Long, url: String): ArrayList<Long>? {
        val usersIDs = ArrayList<Long>()
        val doc = Jsoup.parse(getJson(tweetID, url)!!.getString("htmlUsers"))

        if (doc != null) {
            for (element in doc.getElementsByTag("img")) {
                try {
                    if (element.attr("data-user-id") != "")
                        usersIDs.add(java.lang.Long.parseLong(element.attr("data-user-id")))
                } catch (e: Exception) {
                    e.printStackTrace()
                    return null
                }

            }

            return usersIDs
        }

        return null
    }

    private fun getJson(tweetId: Long, url: String): JSONObject? {
        try {
            val obj = URL(url + tweetId)

            val connection = obj.openConnection() as HttpsURLConnection
            connection.setRequestProperty("Content-Type", "text/html")
            connection.setRequestProperty("charset", "utf-8")
            connection.setRequestProperty("user-agent", USER_AGENT)
            connection.requestMethod = "GET"
            connection.connect()

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val sb = StringBuilder()
            reader.forEachLine { line -> sb.append(line).append('\n') }

            connection.disconnect()
            return JSONObject(sb.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun getBitmapFromURL(strURL: String): Bitmap? {
        try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    fun dpToPx(context: Context, dp: Int) = Math.round(dp *
            (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))

    fun pxToDp(context: Context, px: Int) = Math.round(px /
            (context.resources.displayMetrics.densityDpi.toDouble() / DisplayMetrics.DENSITY_DEFAULT.toDouble())).toInt()

    fun getResourceColorPrimary(context: Context): Int {
        when (AppSettingsImpl.getTheme()) {
            "B" -> return ContextCompat.getColor(context, R.color.blueThemeColorPrimary)
            "P" -> return ContextCompat.getColor(context, R.color.pinkThemeColorPrimary)
            "G" -> return ContextCompat.getColor(context, R.color.greenThemeColorPrimary)
            "D" -> return ContextCompat.getColor(context, R.color.darkThemeColorPrimary)
            else -> return ContextCompat.getColor(context, R.color.blueThemeColorPrimary)
        }
    }

}
