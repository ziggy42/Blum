package com.andreapivetta.blu.data.twitter.model

import android.util.Patterns
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

    fun getTextAsHtmlString(): String {
        val text = getTextWithoutMediaURLs()
        val htmlBuilder = StringBuilder()
        var endString = String()
        text.split("\\r?\\n".toRegex()).dropLastWhile { it.isEmpty() }.forEach { line ->
            if (htmlBuilder.isNotEmpty()) htmlBuilder.append("<br>")
            line.split(" ").dropLastWhile { it.isEmpty() }.forEach { word ->
                if (Patterns.WEB_URL.matcher(word).matches())
                    htmlBuilder.append("<a href=\"").append(word).append("\">").append(word).append("</a>")
                else if (word.isNotEmpty()) {
                    if (word[0] == '@' || word[0] == '.' && word[1] == '@') {
                        val index = word.indexOf('@')
                        var i = index + 1
                        while (i < word.length) {
                            if (isSpecialChar(word[i])) {
                                endString = word.substring(i)
                                break
                            }
                            i++
                        }

                        val word2 = word.substring(index, i)
                        htmlBuilder.append(if (index == 0) "" else ".")
                                .append("<a href=\"com.andreapivetta.blu.user://")
                                .append(word2.substring(1))
                                .append("\">").append(word2).append("</a>")
                                .append(endString)
                    } else if (word[0] == '#') {
                        var word2 = word
                        for (i in 1..word.length - 1)
                            if (isSpecialChar(word[i])) {
                                endString = word.substring(i)
                                word2 = word.substring(0, i)
                                break
                            }

                        htmlBuilder.append("<a href=\"com.andreapivetta.blu.hashtag://")
                                .append(word2.substring(1)).append("\">")
                                .append(word2)
                                .append("</a>").append(endString)
                    } else htmlBuilder.append(word)
                } else htmlBuilder.append(word)
                htmlBuilder.append(" ")
            }
        }

        return htmlBuilder.toString()
    }

    private fun isSpecialChar(char: Char) = "|/()=?'^[],;.:-\"\\".contains(char)

}