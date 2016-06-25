package com.andreapivetta.blu.ui.mediatimeline

import com.andreapivetta.blu.arch.MvpView
import com.andreapivetta.blu.ui.mediatimeline.model.Media

/**
 * Created by andrea on 23/06/16.
 */
interface MediaMvpView : MvpView {

    fun showPhoto(media: Media)

    fun showLoading()

    fun hideLoading()

    fun onNewInteraction()

}