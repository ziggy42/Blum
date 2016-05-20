package com.andreapivetta.blu.ui.new

import com.andreapivetta.blu.arch.MvpView

/**
 * Created by andrea on 20/05/16.
 */
interface NewTweetMvpView : MvpView {

    fun getTweet() : String

    fun showTooManyCharsError()

    fun showSendTweetError()

    fun refreshToolbar()

    fun close()

    fun takePicture()

    fun grabImage()

}