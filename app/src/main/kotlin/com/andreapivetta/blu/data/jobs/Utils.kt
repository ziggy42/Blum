package com.andreapivetta.blu.data.jobs

import com.andreapivetta.blu.data.db.UserId
import io.realm.RealmList
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

/**
 * Created by andrea on 29/07/16.
 */
object Utils {

    private val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36"
    private val FAVORITERS_URL = "https://twitter.com/i/activity/favorited_popup?id="
    private val RETWEETERS_URL = "https://twitter.com/i/activity/retweeted_popup?id="

    @Throws(Exception::class)
    fun getFavoriters(tweetID: Long): RealmList<UserId>? {
        return getUsers(tweetID, FAVORITERS_URL)
    }

    @Throws(Exception::class)
    fun getRetweeters(tweetID: Long): RealmList<UserId>? {
        return getUsers(tweetID, RETWEETERS_URL)
    }

    @Throws(Exception::class)
    private fun getUsers(tweetID: Long, url: String): RealmList<UserId>? {
        val usersIDs = ArrayList<Long>()
        val json = getJson(tweetID, url) ?: return null
        val doc = Jsoup.parse(json.getString("htmlUsers"))

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

            val realmList = RealmList<UserId>()
            usersIDs.map { item -> UserId(item) }.forEach { x -> realmList.add(x) }
            return realmList
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

}