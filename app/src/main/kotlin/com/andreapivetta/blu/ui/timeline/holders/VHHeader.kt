package com.andreapivetta.blu.ui.timeline.holders


import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.decorators.SpaceLeftItemDecoration
import com.andreapivetta.blu.ui.timeline.ImagesAdapter
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TweetInfoProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import twitter4j.Status

class VHHeader(container: View, listener: InteractionListener, tweetInfoProvider: TweetInfoProvider) :
        BaseViewHolder(container, listener, tweetInfoProvider) {

    private val tweetPhotoImageView: ImageView
    private val tweetVideoImageView: ImageView
    private val quotedTweetViewStub: ViewStub
    private val tweetPhotosRecyclerView: RecyclerView
    private val videoCover: FrameLayout
    private val playVideoImageButton: ImageButton

    private var tweetView: View? = null

    init {
        this.tweetPhotoImageView = container.findViewById(R.id.tweetPhotoImageView) as ImageView
        this.quotedTweetViewStub = container.findViewById(R.id.quotedViewStub) as ViewStub
        this.tweetPhotosRecyclerView = container.findViewById(R.id.tweetPhotosRecyclerView) as RecyclerView
        this.tweetVideoImageView = container.findViewById(R.id.tweetVideoImageView) as ImageView
        this.videoCover = container.findViewById(R.id.videoCover) as FrameLayout
        this.playVideoImageButton = container.findViewById(R.id.playVideoImageButton) as ImageButton
    }

    @SuppressLint("NewApi")
    override fun setup(status: Status) {

        val currentUser = status.user

        userNameTextView.text = currentUser.name
        timeTextView.text = formatDate(status.createdAt, container.context)

        Glide.with(container.context).load(currentUser.biggerProfileImageURL)
                .placeholder(R.drawable.placeholder).into(userProfilePicImageView)

        if (status.isFavorited || tweetInfoProvider.isFavorite(status))
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_red_a700_36dp)
        else
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_grey_600_36dp)

        if (status.isRetweeted || tweetInfoProvider.isRetweet(status))
            retweetImageButton.setImageResource(R.drawable.ic_repeat_green_a700_36dp)
        else
            retweetImageButton.setImageResource(R.drawable.ic_repeat_grey600_36dp)

        userProfilePicImageView.setOnClickListener { /* TODO */ }

        favouriteImageButton.setOnClickListener {
            if (status.isFavorited || tweetInfoProvider.isFavorite(status)) {
                listener.unfavorite(status)
                favouriteImageButton.setImageResource(R.drawable.ic_favorite_grey_600_36dp)
            } else {
                listener.favorite(status)
                favouriteImageButton.setImageResource(R.drawable.ic_favorite_red_a700_36dp)
            }
        }

        retweetImageButton.setOnClickListener {
            if (status.isRetweeted || tweetInfoProvider.isRetweet(status)) {
                listener.unretweet(status)
                retweetImageButton.setImageResource(R.drawable.ic_repeat_grey600_36dp)
            } else {
                listener.retweet(status)
                favouriteImageButton.setImageResource(R.drawable.ic_repeat_green_a700_36dp)
            }
        }

        respondImageButton.setOnClickListener { /* TODO */ }

        var text = status.text
        val mediaEntityArray = status.extendedMediaEntities
        for (i in mediaEntityArray.indices)
            text = text.replace(mediaEntityArray[i].url, "")

        val iHateHtml = StringBuilder()
        var endString = ""
        for (line in text.split("\\r?\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if (iHateHtml.length > 0) iHateHtml.append("<br/>")
            for (word in line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                if (Patterns.WEB_URL.matcher(word).matches()) {
                    iHateHtml.append("<a href=\"").append(word).append("\">").append(word).append("</a>")
                } else if (word.length > 1) {
                    if (word[0] == '@' || word[0] == '.' && word[1] == '@') {
                        val index = word.indexOf('@')
                        var i: Int
                        i = index + 1
                        while (i < word.length) {
                            if ("|/()=?'^[],;.:-\"\\".indexOf(word[i]) >= 0) {
                                endString = word.substring(i)
                                break
                            }
                            i++
                        }

                        val word2 = word.substring(index, i)
                        iHateHtml.append(if (index == 0) "" else ".")
                                .append("<a href=\"com.andreapivetta.blu.user://")
                                .append(word2.substring(1))
                                .append("\">")
                                .append(word2)
                                .append("</a>")
                                .append(endString)
                    } else if (word[0] == '#') {
                        var word2 = ""
                        for (i in 1..word.length - 1)
                            if ("|/()=?'^[],;.:-\"\\".indexOf(word[i]) >= 0) {
                                endString = word.substring(i)
                                word2 = word.substring(0, i)
                                break
                            }

                        iHateHtml.append("<a href=\"com.andreapivetta.blu.hashtag://")
                                .append(word2.substring(1))
                                .append("\">")
                                .append(word2)
                                .append("</a>").append(endString)
                    } else {
                        iHateHtml.append(word)
                    }
                } else {
                    iHateHtml.append(word)
                }
                iHateHtml.append(" ")
            }
        }

        statusTextView.text = Html.fromHtml(iHateHtml.toString())
        statusTextView.movementMethod = LinkMovementMethod.getInstance()

        userScreenNameTextView.text = "@" + currentUser.screenName

        var amount = status.favoriteCount.toString() + ""
        var b = StyleSpan(android.graphics.Typeface.BOLD)

        var sb = SpannableStringBuilder(container.context.getString(R.string.likes, amount))
        sb.setSpan(b, 0, amount.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        favouritesStatsTextView.text = sb

        amount = status.retweetCount.toString() + ""
        b = StyleSpan(android.graphics.Typeface.BOLD)

        sb = SpannableStringBuilder(container.context.getString(R.string.retweets, amount))
        sb.setSpan(b, 0, amount.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        retweetsStatsTextView.text = sb

        if (mediaEntityArray.size == 1) {
            val mediaEntity = mediaEntityArray[0]
            if (mediaEntity.type == "photo") {
                tweetPhotoImageView.visibility = View.VISIBLE
                Glide.with(container.context).load(mediaEntity.mediaURL).asBitmap().dontTransform()
                        .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.placeholder)
                        .into(tweetPhotoImageView)

                tweetPhotoImageView.setOnClickListener { /* TODO */ }
            } else {
                videoCover.visibility = View.VISIBLE
                Glide.with(container.context).load(mediaEntity.mediaURL).placeholder(R.drawable.placeholder).centerCrop().into(tweetVideoImageView)

                playVideoImageButton.setOnClickListener { /* TODO */ }
            }
        } else if (mediaEntityArray.size > 1) {
            tweetPhotosRecyclerView.visibility = View.VISIBLE
            tweetPhotosRecyclerView.addItemDecoration(SpaceLeftItemDecoration(5))
            tweetPhotosRecyclerView.adapter = ImagesAdapter(status.extendedMediaEntities, container.context)
            tweetPhotosRecyclerView.layoutManager = LinearLayoutManager(container.context, LinearLayoutManager.HORIZONTAL, false)
        }

        if (status.quotedStatusId > 0) {
            if (tweetView == null)
                tweetView = quotedTweetViewStub.inflate()

            val quotedStatus = status.quotedStatus

            val photoImageView = tweetView!!.findViewById(R.id.photoImageView) as ImageView
            (tweetView!!.findViewById(R.id.quotedUserNameTextView) as TextView).text = quotedStatus.user.name

            if (quotedStatus.mediaEntities.size > 0) {
                photoImageView.visibility = View.VISIBLE
                Glide.with(container.context).load(quotedStatus.mediaEntities[0].mediaURL).placeholder(R.drawable.placeholder).into(photoImageView)

                (tweetView!!.findViewById(R.id.quotedStatusTextView) as TextView).text = quotedStatus.text.replace(quotedStatus.mediaEntities[0].url, "")
            } else {
                photoImageView.visibility = View.GONE
                (tweetView!!.findViewById(R.id.quotedStatusTextView) as TextView).text = quotedStatus.text
            }

            tweetView!!.setOnClickListener { /* TODO */ }

        }
    }
}
