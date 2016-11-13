package com.andreapivetta.blu.ui.mediatimeline

import com.andreapivetta.blu.ui.mediatimeline.model.Media

/**
 * Created by andrea on 13/11/16.
 */
interface MediaListener {

    fun favorite(media: Media)

    fun retweet(media: Media)

    fun unfavorite(media: Media)

    fun unretweet(media: Media)
}