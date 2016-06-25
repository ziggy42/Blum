package com.andreapivetta.blu.ui.mediatimeline

import com.andreapivetta.blu.arch.MvpView

/**
 * Created by andrea on 23/06/16.
 */
interface MediaMvpView : MvpView {

    fun showPhotos(photos: MutableList<String>)

    fun showPhoto(photo: String)

    fun showLoading()

    fun hideLoading()

}