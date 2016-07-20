package com.andreapivetta.blu.data.twitter.model

import twitter4j.ExtendedMediaEntity
import twitter4j.Status
import twitter4j.User
import java.io.Serializable

/**
 * Created by andrea on 20/07/16.
 */
data class Tweet(val status: Status) : Serializable {

    val text: String = status.text
    val id: Long = status.id
    val timeStamp: Long = status.createdAt.time
    val inReplayToStatusId: Long = status.inReplyToStatusId
    val user: User = status.user
    val quotedStatus: Boolean = status.quotedStatus != null
    val quotedStatusId: Long = status.quotedStatusId
    val retweet: Boolean = status.isRetweet
    val mediaEntities: Array<ExtendedMediaEntity> = status.extendedMediaEntities

    var retweetedByMe: Boolean = status.isRetweetedByMe
    var retweeted: Boolean = status.isRetweeted
    var retweetCount: Int = status.retweetCount
    var favorited: Boolean = status.isFavorited
    var favoriteCount: Int = status.favoriteCount

    fun getRetweetedTweet(): Tweet {
        if (retweet)
            return Tweet(status.retweetedStatus)
        throw RuntimeException("This Tweet is not a Retweet")
    }

    fun getQuotedTweet(): Tweet {
        if (quotedStatus)
            return Tweet(status.quotedStatus)
        throw RuntimeException("This Tweet does not contain a quoted Tweet")
    }

    fun hasSingleImage() = mediaEntities.size == 1 && "photo".equals(mediaEntities[0].type)

    fun hasSingleVideo() = mediaEntities.size == 1 && !"photo".equals(mediaEntities[0].type)

    fun hasMultipleMedia() = mediaEntities.size > 1

    fun getImageUrl(): String {
        if (hasSingleImage() || hasMultipleMedia())
            return mediaEntities[0].mediaURL
        throw RuntimeException("This Tweet does not have a photo")
    }

    fun getVideoCoverUrl(): String {
        if (hasSingleVideo() || hasMultipleMedia())
            return mediaEntities[0].mediaURL
        throw RuntimeException("This Tweet does not have a video")
    }

    fun getVideoUrlType(): Pair<String, String> {
        if (hasSingleVideo() || hasMultipleMedia())
            return Pair(mediaEntities[0].videoVariants[0].url, mediaEntities[0].type)
        throw RuntimeException("This Tweet does not have a video")
    }

    fun getTextWithoutMediaURLs(): String {
        var noUrlText = text
        for (i in mediaEntities.indices)
            noUrlText = text.replace(mediaEntities[i].url, "")
        return noUrlText
    }

}