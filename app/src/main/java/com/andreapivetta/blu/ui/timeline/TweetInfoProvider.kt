package com.andreapivetta.blu.ui.timeline

import twitter4j.Status

/**
 * Created by andrea on 22/05/16.
 */
interface TweetInfoProvider {
    fun isFavorite(status: Status): Boolean

    fun isRetweet(status: Status): Boolean
}