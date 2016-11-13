package com.andreapivetta.blu.common.utils

import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.bumptech.glide.Glide

/**
 * Created by andrea on 27/09/16.
 */
fun View.visible(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun ImageView.loadUrl(url: String, @DrawableRes placeholder: Int = R.drawable.placeholder) {
    Glide.with(context).load(url).placeholder(placeholder).into(this)
}

fun ImageView.loadUrlCenterCrop(url: String, @DrawableRes placeholder: Int = R.drawable.placeholder) {
    Glide.with(context).load(url).placeholder(placeholder).centerCrop().into(this)
}

fun ImageView.loadAvatar(url: String) {
    // TODO placeholder
    Glide.with(context).load(url).dontAnimate().into(this)
}

fun AppCompatActivity.pushFragment(@LayoutRes containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(containerViewId, fragment).commit()
}