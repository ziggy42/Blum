package com.andreapivetta.blu.data.twitter

import com.andreapivetta.blu.data.model.Tweet

/**
 * Created by andrea on 12/11/16.
 */
fun getTweetUrl(tweet: Tweet) = "https://twitter.com/blum/status/${tweet.id}"

fun getFavoritersUrl(id: Long) = "https://twitter.com/i/activity/favorited_popup?id=$id"

fun getRetweetersUrl(id: Long) = "https://twitter.com/i/activity/retweeted_popup?id=$id"
