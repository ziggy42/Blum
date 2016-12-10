package com.andreapivetta.blu.common.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.custom.Theme
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

fun ImageView.loadUrlWithoutPlaceholder(url: CharSequence?) {
    Glide.with(context).load(url).into(this)
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
            .setToolbarColor(Theme.getColorPrimary(activity))
            .setShowTitle(true)
            .addDefaultShareMenuItem()
            .build()
            .launchUrl(activity, Uri.parse(url.trim()))
}

fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    intent.type = "text/plain"
    context.startActivity(intent)
}

fun download(context: Context, url: String) {
    val request = DownloadManager.Request(Uri.parse(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir("/Download", System.currentTimeMillis().toString())

    request.allowScanningByMediaScanner()

    (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
}