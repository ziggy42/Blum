package com.andreapivetta.blu.ui.newtweet

import com.andreapivetta.blu.ui.base.MvpView

/**
 * Created by andrea on 20/05/16.
 */
interface NewTweetMvpView : MvpView {

    fun getTweet() : String

    fun setText(text: String?, selection: Int)

    fun getSelectionStart(): Int

    fun showTooManyCharsError()

    fun showTooManyImagesError()

    fun showSendTweetError()

    fun refreshToolbar()

    fun filterUsers(prefix: String)

    fun showSuggestions()

    fun hideSuggestions()

    fun close()

    fun takePicture()

    fun grabImage()

}