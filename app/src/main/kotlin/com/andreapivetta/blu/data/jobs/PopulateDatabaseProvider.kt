package com.andreapivetta.blu.data.jobs

import com.andreapivetta.blu.common.pref.AppSettingsImpl
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.data.db.*
import io.realm.RealmList
import org.json.JSONObject
import org.jsoup.Jsoup
import twitter4j.PagableResponseList
import twitter4j.Paging
import twitter4j.Twitter
import twitter4j.User
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection

/**
 * Created by andrea on 17/09/16.
 */
object PopulateDatabaseProvider {

    private val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36"
    private val FAVORITERS_URL = "https://twitter.com/i/activity/favorited_popup?id="
    private val RETWEETERS_URL = "https://twitter.com/i/activity/retweeted_popup?id="

    fun retrieveTweetInfo(twitter: Twitter) {
        twitter.getUserTimeline(Paging(1, 200))
                .forEach { x ->
                    run {
                        val favs = getFavoriters(x.id)
                        val retws = getRetweeters(x.id)
                        Utils.runOnUiThread {
                            AppStorageImpl.saveTweetInfo(TweetInfo(x.id, favs, retws))
                        }
                    }
                }
        AppSettingsImpl.setFavRetDownloaded(true)
    }

    fun retrieveMentions(twitter: Twitter) {
        val mentions = twitter.getMentionsTimeline(Paging(1, 200))
                .map { x -> Mention.valueOf(x) }
        Utils.runOnUiThread { AppStorageImpl.saveMentions(mentions) }
        AppSettingsImpl.setMentionsDownloaded(true)
    }

    fun retrieveFollowers(twitter: Twitter) {
        val ids = twitter.getFollowersIDs(-1)
        do {
            val followers = ids.iDs.map { x -> Follower(x) }
            Utils.runOnUiThread { AppStorageImpl.saveFollowers(followers) }
        } while (ids.hasNext())
        AppSettingsImpl.setFollowersDownloaded(true)
    }

    fun retrievePrivateMessages(twitter: Twitter) {
        val directMessages: MutableList<PrivateMessage> = twitter.getDirectMessages(Paging(1, 200))
                .map { x -> PrivateMessage.valueOf(x) }
                .toMutableList()
        directMessages.addAll(twitter.getSentDirectMessages(Paging(1, 200))
                .map { x -> PrivateMessage.valueOf(x) })
        Utils.runOnUiThread { AppStorageImpl.savePrivateMessages(directMessages) }
        AppSettingsImpl.setDirectMessagesDownloaded(true)
    }

    fun safeRetrieveFollowedUsers(twitter: Twitter) {
        if (twitter.showUser(twitter.id).friendsCount < 2800) {
            var cursor: Long = -1
            var pagableFollowings: PagableResponseList<User>
            do {
                pagableFollowings = twitter.getFriendsList(twitter.id, cursor, 200)

                val users = pagableFollowings.map { x -> UserFollowed.valueOf(x) }
                Utils.runOnUiThread { AppStorageImpl.saveUsersFollowed(users) }
                cursor = pagableFollowings.nextCursor
            } while (cursor != 0L)

            AppSettingsImpl.setUserFollowedAvailable(true)
        }
    }

    @Throws(Exception::class)
    fun getFavoriters(tweetID: Long): RealmList<UserId>? = getUsers(tweetID, FAVORITERS_URL)

    @Throws(Exception::class)
    fun getRetweeters(tweetID: Long): RealmList<UserId>? = getUsers(tweetID, RETWEETERS_URL)

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
            realmList.addAll(usersIDs.map { item -> UserId(item) })
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