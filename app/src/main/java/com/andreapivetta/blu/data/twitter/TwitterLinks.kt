package com.andreapivetta.blu.data.twitter

import com.andreapivetta.blu.data.model.Tweet

/**
 * Created by andrea on 12/11/16.
 */
fun getTweetLink(tweet: Tweet) = "https://twitter.com/blum/status/${tweet.id}"