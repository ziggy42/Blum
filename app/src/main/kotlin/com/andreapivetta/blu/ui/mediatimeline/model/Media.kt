package com.andreapivetta.blu.ui.mediatimeline.model

import twitter4j.Status
import java.io.Serializable

/**
 * Created by andrea on 25/06/16.
 */
class Media(status: Status) : Serializable {

    val tweetId: Long
    val mediaUrl: String
    var retweet: Boolean
    var favorite: Boolean

    init {
        tweetId = status.id
        mediaUrl = status.mediaEntities[0].mediaURLHttps
        retweet = status.isRetweeted
        favorite = status.isFavorited
    }
}