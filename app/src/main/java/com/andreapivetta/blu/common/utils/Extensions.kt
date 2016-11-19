package com.andreapivetta.blu.common.utils

import android.app.Activity
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.bumptech.glide.Glide
import java.io.File

/**
 * Created by andrea on 27/09/16.
 */
fun View.visible(show: Boolean = true) {
    this.visibility = if (show) View.VISIBLE else View.GONE
}

fun ImageView.loadFile(file: File?, @DrawableRes placeholder: Int = R.drawable.placeholder) {
    Glide.with(context).load(file).placeholder(placeholder).into(this)
}

fun ImageView.loadUrl(url: CharSequence?, @DrawableRes placeholder: Int = R.drawable.placeholder) {
    Glide.with(context).load(url).placeholder(placeholder).into(this)
}

fun ImageView.loadUrlCenterCrop(url: CharSequence?, @DrawableRes placeholder: Int = R.drawable.placeholder) {
    Glide.with(context).load(url).placeholder(placeholder).centerCrop().into(this)
}

fun ImageView.loadAvatar(url: CharSequence?) {
    // TODO placeholder
    Glide.with(context).load(url).dontAnimate().into(this)
}

fun AppCompatActivity.pushFragment(@LayoutRes containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(containerViewId, fragment).commit()
}

fun openUrl(activity: Activity, url: String) {
    CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(activity, R.color.blueThemeColorPrimary))
            .setShowTitle(true)
            .build()
            .launchUrl(activity, Uri.parse(url.trim()))
}