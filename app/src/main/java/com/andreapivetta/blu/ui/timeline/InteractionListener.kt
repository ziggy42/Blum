package com.andreapivetta.blu.ui.timeline

import com.andreapivetta.blu.data.model.Tweet
import twitter4j.User

/**
 * Created by andrea on 22/05/16.
 */
interface InteractionListener {

    fun favorite(tweet: Tweet)

    fun retweet(tweet: Tweet)

    fun unfavorite(tweet: Tweet)

    fun unretweet(tweet: Tweet)

    fun reply(tweet: Tweet, user: User)

    fun openTweet(tweet: Tweet)

    fun showUser(user: User)

    fun showImage(imageUrl: String)

    fun showImages(imageUrls: List<String>, index: Int)

    fun showVideo(videoUrl: String, videoType: String)

}