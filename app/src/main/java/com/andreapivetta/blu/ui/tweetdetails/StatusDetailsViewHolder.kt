package com.andreapivetta.blu.ui.tweetdetails

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.*
import com.andreapivetta.blu.data.model.MetaData
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.custom.Theme
import com.andreapivetta.blu.ui.custom.decorators.SpaceLeftItemDecoration
import com.andreapivetta.blu.ui.hashtag.HashtagActivity
import com.andreapivetta.blu.ui.profile.UserActivity
import com.andreapivetta.blu.ui.timeline.holders.BaseViewHolder
import com.andreapivetta.blu.ui.timeline.holders.ImagesAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.luseen.autolinklibrary.AutoLinkMode
import com.luseen.autolinklibrary.AutoLinkTextView
import com.schinizer.rxunfurl.RxUnfurl
import kotlinx.android.synthetic.main.tweet_big.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by andrea on 26/05/16.
 */
class StatusDetailsViewHolder(container: View, listener: DetailsInteractionListener) :
        BaseViewHolder(container, listener) {

    private val mediaViewStub = container.mediaViewStub
    private val quotedStatusViewStub = container.quotedStatusViewStub
    private val urlPreviewViewStub = container.urlPreviewViewStub
    private val shareImageButton = container.shareImageButton
    private val quoteImageButton = container.quoteImageButton
    private val context: Context = container.context

    private var inflatedMediaView: View? = null
    private var inflatedQuotedView: View? = null
    private var inflatedUrlPreviewView: View? = null

    override fun setup(tweet: Tweet) {
        val currentUser = tweet.user

        val listener = listener as DetailsInteractionListener
        val autolinkTextView = statusTextView as AutoLinkTextView

        userNameTextView.text = currentUser.name
        timeTextView.text = Utils.formatDate(tweet.timeStamp)

        autolinkTextView.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_URL,
                AutoLinkMode.MODE_MENTION)
        autolinkTextView.setHashtagModeColor(Theme.getColorPrimary(context))
        autolinkTextView.setUrlModeColor(Theme.getColorPrimary(context))
        autolinkTextView.setMentionModeColor(Theme.getColorPrimary(context))
        autolinkTextView.setAutoLinkText(tweet.text)
        autolinkTextView.setAutoLinkOnClickListener { mode, text ->
            when (mode) {
                AutoLinkMode.MODE_HASHTAG -> HashtagActivity.launch(container.context, text)
                AutoLinkMode.MODE_MENTION -> UserActivity.launch(container.context, text)
                AutoLinkMode.MODE_URL -> openUrl(container.context as Activity, text)
                else -> throw UnsupportedOperationException("No handlers for mode $mode")
            }
        }

        userScreenNameTextView.text = "@${currentUser.screenName}"

        var amount = "${tweet.favoriteCount}"
        var b = StyleSpan(Typeface.BOLD)

        var sb = SpannableStringBuilder(container.context.getString(R.string.likes, amount))
        sb.setSpan(b, 0, amount.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        favouritesStatsTextView.text = sb

        amount = "${tweet.retweetCount}"
        b = StyleSpan(Typeface.BOLD)

        sb = SpannableStringBuilder(container.context.getString(R.string.retweets, amount))
        sb.setSpan(b, 0, amount.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        retweetsStatsTextView.text = sb

        userProfilePicImageView.loadAvatar(currentUser.biggerProfileImageURL)

        if (tweet.favorited)
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_red)
        else
            favouriteImageButton.setImageResource(R.drawable.ic_favorite)

        if (tweet.retweeted)
            retweetImageButton.setImageResource(R.drawable.ic_repeat_green)
        else
            retweetImageButton.setImageResource(R.drawable.ic_repeat)

        favouriteImageButton.setOnClickListener {
            if (tweet.favorited)
                listener.unfavorite(tweet)
            else
                listener.favorite(tweet)
        }

        retweetImageButton.setOnClickListener {
            if (tweet.retweeted)
                listener.unretweet(tweet)
            else
                listener.retweet(tweet)
        }

        userProfilePicImageView.setOnClickListener { listener.showUser(currentUser) }
        respondImageButton.setOnClickListener { listener.reply(tweet, currentUser) }
        shareImageButton.setOnClickListener { listener.shareTweet(tweet) }
        quoteImageButton.setOnClickListener { listener.quoteTweet(tweet) }

        when {
            tweet.hasSingleImage() -> setupPhoto(tweet)
            tweet.hasMultipleMedia() -> setupPhotos(tweet)
            tweet.hasSingleVideo() -> setupVideo(tweet)
        }

        when {
            tweet.quotedStatus -> setupQuotedStatus(tweet)
            tweet.hasLinks() -> setupUrlPreview(tweet)
        }
    }

    private fun setupPhoto(tweet: Tweet) {
        if (inflatedMediaView == null) {
            mediaViewStub.layoutResource = R.layout.stub_photo
            inflatedMediaView = mediaViewStub.inflate()
        }

        Glide.with(container.context).load(tweet.getImageUrl())
                .asBitmap().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(inflatedMediaView as ImageView)
        inflatedMediaView?.setOnClickListener { listener.showImage(tweet.getImageUrl()) }
    }

    private fun setupPhotos(tweet: Tweet) {
        if (inflatedMediaView == null) {
            mediaViewStub.layoutResource = R.layout.stub_photos
            inflatedMediaView = mediaViewStub.inflate()
        }

        val recyclerView = inflatedMediaView as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceLeftItemDecoration(5))
        recyclerView.adapter = ImagesAdapter(tweet.mediaEntities, listener)
        recyclerView.layoutManager = LinearLayoutManager(container.context,
                LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupVideo(tweet: Tweet) {
        if (inflatedMediaView == null) {
            mediaViewStub.layoutResource = R.layout.video_cover
            inflatedMediaView = mediaViewStub.inflate()
        }

        (inflatedMediaView?.findViewById(R.id.tweetVideoImageView) as ImageView)
                .loadUrlCenterCrop(tweet.getVideoCoverUrl())

        inflatedMediaView?.findViewById(R.id.playVideoImageButton)?.setOnClickListener {
            val pair = tweet.getVideoUrlType()
            listener.showVideo(pair.first, pair.second)
        }
    }

    private fun setupQuotedStatus(tweet: Tweet) {
        if (inflatedQuotedView == null) {
            quotedStatusViewStub.layoutResource = R.layout.quoted_tweet
            inflatedQuotedView = quotedStatusViewStub.inflate()
        }

        val quotedStatus = tweet.getQuotedTweet()

        val photoImageView = inflatedQuotedView?.findViewById(R.id.photoImageView) as ImageView
        (inflatedQuotedView?.findViewById(R.id.quotedUserNameTextView) as TextView).text =
                quotedStatus.user.name

        // TODO other medias
        if (quotedStatus.hasSingleImage()) {
            photoImageView.visible()
            photoImageView.loadUrl(quotedStatus.getImageUrl())

            (inflatedQuotedView?.findViewById(R.id.quotedStatusTextView) as TextView).text =
                    quotedStatus.getTextWithoutMediaURLs()
        } else {
            (inflatedQuotedView as View).visible(false)
            (inflatedQuotedView?.findViewById(R.id.quotedStatusTextView) as TextView).text =
                    quotedStatus.text
        }

        inflatedQuotedView?.setOnClickListener {
            listener.openTweet(quotedStatus, quotedStatus.user)
        }
    }

    private fun setupUrlPreview(tweet: Tweet) {
        if (inflatedUrlPreviewView == null) {
            urlPreviewViewStub.layoutResource = R.layout.url_preview
            inflatedUrlPreviewView = urlPreviewViewStub.inflate()
        }

        val previewImageView = inflatedUrlPreviewView
                ?.findViewById(R.id.urlPreviewImageView) as ImageView
        val descriptionTextView = inflatedUrlPreviewView
                ?.findViewById(R.id.urlDescriptionTextView) as TextView
        val titleTextView = inflatedUrlPreviewView?.findViewById(R.id.urlTitleTextView) as TextView
        val loadingProgressBar = inflatedUrlPreviewView?.findViewById(R.id.loadingProgressBar)

        RxUnfurl.generatePreview(tweet.getLink())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ x ->
                    run {
                        tweet.metaData =
                                MetaData(if (x.images.isNotEmpty()) x.images[0].source
                                else null, x.title, x.description, tweet.getLink())

                        loadingProgressBar?.visible(false)
                        if (x.images.isNotEmpty())
                            previewImageView.loadUrl(x.images[0].source)
                        else
                            previewImageView.visible(false)
                        titleTextView.text = x.title
                        descriptionTextView.text = x.description
                        inflatedUrlPreviewView?.setOnClickListener { openUrl(container.context as Activity, x.url) }
                    }
                }, { e -> Timber.e(e, "Error loading url preview") })
    }

}