package com.andreapivetta.blu.ui.timeline.holders

import android.app.Activity
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.data.model.MetaData
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.schinizer.rxunfurl.RxUnfurl
import kotlinx.android.synthetic.main.url_preview.view.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by andrea on 06/10/16.
 */
class StatusLinkViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val urlPreviewImageView: ImageView = container.urlPreviewImageView
    private val urlTitleTextView: TextView = container.urlTitleTextView
    private val urlTextDescriptionView: TextView = container.urlDescriptionTextView

    private var subscriber: Subscription? = null

    override fun setup(tweet: Tweet) {
        super.setup(tweet)

        urlPreviewImageView.setImageDrawable(
                ContextCompat.getDrawable(container.context, R.drawable.placeholder))
        urlTitleTextView.text = ""
        urlTextDescriptionView.text = ""

        if (tweet.metaData == null) {
            subscriber?.unsubscribe()
            subscriber = RxUnfurl.generatePreview(tweet.getLink())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ x ->
                        run {
                            tweet.metaData = MetaData(x.images[0].source, x.title,
                                    x.description, tweet.getLink())
                            loadPreview(tweet.metaData as MetaData)
                        }
                    }, { e -> Timber.e(e, "Error loading url preview") })
        } else loadPreview(tweet.metaData as MetaData)
    }

    private fun loadPreview(metaData: MetaData) {
        urlPreviewImageView.loadUrl(metaData.imageUrl)
        urlTitleTextView.text = metaData.title
        urlTextDescriptionView.text = metaData.imageUrl
        container.setOnClickListener {
            CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat
                            .getColor(container.context, R.color.blueThemeColorPrimary))
                    .build()
                    .launchUrl(container.context as Activity, Uri.parse(metaData.link))
        }
    }


}