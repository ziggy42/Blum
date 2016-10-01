package com.andreapivetta.blu.common.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by andrea on 27/09/16.
 */
fun View.visible(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).into(this)
}