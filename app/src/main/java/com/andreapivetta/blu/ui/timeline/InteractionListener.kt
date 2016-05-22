package com.andreapivetta.blu.ui.timeline

import twitter4j.Status
import twitter4j.User

/**
 * Created by andrea on 22/05/16.
 */
interface InteractionListener {

    fun favorite(status: Status)

    fun retweet(status: Status)

    fun unfavorite(status: Status)

    fun unretweet(status: Status)

    fun replay(status: Status)

    fun openTweet(status: Status)

    fun showUser(user: User)

}