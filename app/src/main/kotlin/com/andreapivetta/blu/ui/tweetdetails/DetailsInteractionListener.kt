package com.andreapivetta.blu.ui.tweetdetails

import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener

/**
 * Created by andrea on 11/11/16.
 */
interface DetailsInteractionListener : InteractionListener {

    fun shareTweet(tweet: Tweet)

}