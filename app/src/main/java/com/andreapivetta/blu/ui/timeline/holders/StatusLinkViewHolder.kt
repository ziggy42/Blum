package com.andreapivetta.blu.ui.timeline.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.common.utils.openUrl
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.MetaData
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.schinizer.rxunfurl.RxUnfurl
import kotlinx.android.synthetic.main.tweet_link.view.*
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
    private val urlPreviewLayout = container.urlPreviewLayout
    private val loadingProgressBar = container.loadingProgressBar
    private var subscriber: Subscription? = null

    override fun setup(tweet: Tweet) {
        super.setup(tweet)

        setLoading(true)
        if (tweet.metaData == null) {
            urlPreviewLayout.setOnClickListener { }
            subscriber?.unsubscribe()
            subscriber = RxUnfurl.generatePreview(tweet.getLink())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ x ->
                        run {
                            tweet.metaData =
                                    MetaData(if (x.images.isNotEmpty()) x.images[0].source
                                    else null, x.title, x.description, tweet.getLink())
                            loadPreview(tweet.metaData as MetaData)
                        }
                    }, { e -> Timber.e(e, "Error loading url preview") })
        } else loadPreview(tweet.metaData as MetaData)
    }

    private fun setLoading(isLoading: Boolean) {
        loadingProgressBar.visible(isLoading)
        urlPreviewImageView.visible(!isLoading)
        urlTitleTextView.visible(!isLoading)
        urlTextDescriptionView.visible(!isLoading)
    }

    private fun loadPreview(metaData: MetaData) {
        setLoading(false)
        if (metaData.imageUrl != null)
            urlPreviewImageView.loadUrl(metaData.imageUrl)
        else
            urlPreviewImageView.visibility = View.GONE
        urlTitleTextView.text = metaData.title
        urlTextDescriptionView.text = metaData.description
        urlPreviewLayout.setOnClickListener { openUrl(container.context, metaData.link) }
    }
}