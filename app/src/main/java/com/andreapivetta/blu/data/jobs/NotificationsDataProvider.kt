package com.andreapivetta.blu.data.jobs

import com.andreapivetta.blu.data.model.*
import com.andreapivetta.blu.data.twitter.getFavoritersUrl
import com.andreapivetta.blu.data.twitter.getRetweetersUrl
import io.realm.RealmList
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import timber.log.Timber
import twitter4j.*
import java.net.SocketTimeoutException
import java.util.*

/**
 * Created by andrea on 17/09/16.
 */
object NotificationsDataProvider {

    private val httpClient = OkHttpClient.Builder()
            .addInterceptor {
                it.proceed(it.request().newBuilder().addHeader("user-agent", "Mozilla/5.0(X11; " +
                        "Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36").build())
            }.build()

    fun retrieveTweetInfo(twitter: Twitter): MutableList<TweetInfo> =
            twitter.getUserTimeline(Paging(1, 100))
                    .map { TweetInfo(it.id, getFavoriters(it.id), getRetweeters(it.id)) }
                    .toMutableList()

    fun retrieveMentions(twitter: Twitter): MutableList<Mention> =
            twitter.getMentionsTimeline(Paging(1, 200))
                    .map { Mention.valueOf(it) }
                    .toMutableList()

    fun retrieveFollowers(twitter: Twitter): MutableList<Follower> {
        val followers = ArrayList<Follower>()
        val ids = twitter.getFollowersIDs(-1)
        do followers.addAll(ids.iDs.map(::Follower)) while (ids.hasNext())
        return followers
    }

    fun retrieveDirectMessages(twitter: Twitter): MutableList<DirectMessage> {
        val directMessages = twitter.getDirectMessages(Paging(1, 200))
        directMessages.addAll(twitter.getSentDirectMessages(Paging(1, 200)))
        return directMessages
    }

    fun safeRetrieveFollowedUsers(twitter: Twitter): MutableList<UserFollowed> {
        val users = ArrayList<UserFollowed>()
        if (twitter.showUser(twitter.id).friendsCount < 2800) {
            var cursor: Long = -1
            var pagableFollowings: PagableResponseList<User>
            do {
                pagableFollowings = twitter.getFriendsList(twitter.id, cursor, 200)
                users.addAll(pagableFollowings.map { UserFollowed.valueOf(it) })
                cursor = pagableFollowings.nextCursor
            } while (cursor != 0L)
        }
        return users
    }

    fun getFavoriters(tweetID: Long): RealmList<UserId>? = getUsers(getFavoritersUrl(tweetID))

    fun getRetweeters(tweetID: Long): RealmList<UserId>? = getUsers(getRetweetersUrl(tweetID))

    private fun getUsers(url: String): RealmList<UserId>? = try {
        val json = getJson(url)
        RealmList(*Jsoup.parse(json.getString("htmlUsers"))
                .getElementsByTag("img")
                .filter { it.hasAttr("data-user-id") && it.attr("data-user-id").isNotBlank() }
                .map { UserId(it.attr("data-user-id").toLong()) }
                .toTypedArray())
    } catch (exception: JSONException) {
        Timber.e(exception, "Error getting users!")
        null
    } catch (exception: SocketTimeoutException) {
        Timber.e(exception, "Timeout getting users!")
        null
    }

    @Throws(JSONException::class, SocketTimeoutException::class)
    private fun getJson(url: String): JSONObject =
            JSONObject(httpClient.newCall(Request.Builder().url(url).build()).execute().body().string())

}