package com.andreapivetta.blu.ui.mediatimeline.model

import twitter4j.Status
import java.io.Serializable

/**
 * Created by andrea on 25/06/16.
 */
class Media(status: Status) : Serializable {

    val tweetId = status.id
    val mediaUrl = status.mediaEntities[0].mediaURLHttps
    var retweet = status.isRetweeted
    var favorite = status.isFavorited

}