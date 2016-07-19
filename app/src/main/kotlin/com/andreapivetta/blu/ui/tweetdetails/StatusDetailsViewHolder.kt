package com.andreapivetta.blu.ui.tweetdetails

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.decorators.SpaceLeftItemDecoration
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TweetInfoProvider
import com.andreapivetta.blu.ui.timeline.holders.BaseViewHolder
import com.andreapivetta.blu.ui.timeline.holders.ImagesAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import twitter4j.Status

/**
 * Created by andrea on 26/05/16.
 */
class StatusDetailsViewHolder(container: View, listener: InteractionListener, tweetInfoProvider: TweetInfoProvider) :
        BaseViewHolder(container, listener, tweetInfoProvider) {

    private val mediaViewStub: ViewStub
    private val quotedStatusViewStub: ViewStub

    init {
        this.mediaViewStub = container.findViewById(R.id.mediaViewStub) as ViewStub
        this.quotedStatusViewStub = container.findViewById(R.id.quotedStatusViewStub) as ViewStub
    }

    private var inflatedMediaView: View? = null
    private var inflatedQuotedView: View? = null

    override fun setup(status: Status) {
        val currentUser = status.user

        userNameTextView.text = currentUser.name
        timeTextView.text = formatDate(status.createdAt, container.context)
        statusTextView.text = getHtmlStatusText(status)
        statusTextView.movementMethod = LinkMovementMethod.getInstance()

        userScreenNameTextView.text = "@" + currentUser.screenName

        var amount = status.favoriteCount.toString()
        var b = StyleSpan(Typeface.BOLD)

        var sb = SpannableStringBuilder(container.context.getString(R.string.likes, amount))
        sb.setSpan(b, 0, amount.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        favouritesStatsTextView.text = sb

        amount = status.retweetCount.toString()
        b = StyleSpan(Typeface.BOLD)

        sb = SpannableStringBuilder(container.context.getString(R.string.retweets, amount))
        sb.setSpan(b, 0, amount.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        retweetsStatsTextView.text = sb

        Glide.with(container.context).load(currentUser.biggerProfileImageURL)
                .dontAnimate().placeholder(R.drawable.placeholder).into(userProfilePicImageView)

        if (status.isFavorited || tweetInfoProvider.isFavorite(status))
            favouriteImageButton.setImageResource(R.drawable.ic_favorite_red)
        else
            favouriteImageButton.setImageResource(R.drawable.ic_favorite)

        if (status.isRetweeted || tweetInfoProvider.isRetweet(status))
            retweetImageButton.setImageResource(R.drawable.ic_repeat_green)
        else
            retweetImageButton.setImageResource(R.drawable.ic_repeat)

        favouriteImageButton.setOnClickListener {
            if (status.isFavorited || tweetInfoProvider.isFavorite(status))
                listener.unfavorite(status)
            else
                listener.favorite(status)
        }

        retweetImageButton.setOnClickListener {
            if (status.isRetweeted || tweetInfoProvider.isRetweet(status))
                listener.unretweet(status)
            else
                listener.retweet(status)
        }

        userProfilePicImageView.setOnClickListener { listener.showUser(currentUser) }
        respondImageButton.setOnClickListener { listener.reply(status, currentUser) }

        if (hasPhoto(status)) {
            if (inflatedMediaView == null) {
                mediaViewStub.layoutResource = R.layout.stub_photo
                inflatedMediaView = mediaViewStub.inflate()
            }

            Glide.with(container.context).load(status.extendedMediaEntities[0].mediaURL)
                    .asBitmap().dontTransform()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .into(inflatedMediaView as ImageView)
            inflatedMediaView?.setOnClickListener { listener.showImage(status.extendedMediaEntities[0].mediaURL) }
        } else if (hasPhotos(status)) {
            if (inflatedMediaView == null) {
                mediaViewStub.layoutResource = R.layout.stub_photos
                inflatedMediaView = mediaViewStub.inflate()
            }

            val recyclerView = inflatedMediaView as RecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(SpaceLeftItemDecoration(5))
            recyclerView.adapter = ImagesAdapter(status.extendedMediaEntities, listener)
            recyclerView.layoutManager = LinearLayoutManager(container.context, LinearLayoutManager.HORIZONTAL, false)
        } else if (hasVideo(status)) {
            if (inflatedMediaView == null) {
                mediaViewStub.layoutResource = R.layout.video_cover
                inflatedMediaView = mediaViewStub.inflate()
            }

            Glide.with(container.context).load(status.extendedMediaEntities[0].mediaURL)
                    .asBitmap().dontTransform()
                    .placeholder(R.drawable.placeholder)
                    .centerCrop().into(inflatedMediaView?.findViewById(R.id.tweetVideoImageView) as ImageView)

            inflatedMediaView?.findViewById(R.id.playVideoImageButton)?.setOnClickListener {
                listener.showVideo(status.extendedMediaEntities[0].videoVariants[0].url,
                        status.extendedMediaEntities[0].type)
            }
        }

        if (hasQuotedStatus(status)) {
            if (inflatedQuotedView == null) {
                quotedStatusViewStub.layoutResource = R.layout.quoted_tweet
                inflatedQuotedView = quotedStatusViewStub.inflate()
            }

            val quotedStatus = status.quotedStatus

            val photoImageView = inflatedQuotedView!!.findViewById(R.id.photoImageView) as ImageView
            (inflatedQuotedView?.findViewById(R.id.quotedUserNameTextView) as TextView).text =
                    quotedStatus.user.name

            if (quotedStatus.mediaEntities.size > 0) {
                photoImageView.visibility = View.VISIBLE
                Glide.with(container.context).load(quotedStatus.mediaEntities[0].mediaURL)
                        .asBitmap().dontTransform()
                        .placeholder(R.drawable.placeholder).into(photoImageView)

                (inflatedQuotedView?.findViewById(R.id.quotedStatusTextView) as TextView).text =
                        quotedStatus.text.replace(quotedStatus.mediaEntities[0].url, "")
            } else {
                inflatedQuotedView?.visibility = View.GONE
                (inflatedQuotedView!!.findViewById(R.id.quotedStatusTextView) as TextView).text =
                        quotedStatus.text
            }

            inflatedQuotedView?.setOnClickListener { listener.openTweet(quotedStatus, quotedStatus.user) }
        }
    }

    private fun hasPhoto(status: Status) =
            status.extendedMediaEntities.size == 1 && status.extendedMediaEntities[0].type == "photo"

    private fun hasPhotos(status: Status): Boolean = status.extendedMediaEntities.size > 1

    private fun hasVideo(status: Status) =
            status.extendedMediaEntities.size == 1 && status.extendedMediaEntities[0].type != "photo"

    private fun hasQuotedStatus(status: Status) = status.quotedStatusId > 0

    private fun getHtmlStatusText(status: Status): Spanned {
        val text = status.text
        status.mediaEntities.forEach { media -> text.replace(media.url, "") }
        val htmlBuilder = StringBuilder()
        var endString = String()
        text.split("\\r?\\n".toRegex()).dropLastWhile { it.isEmpty() }.forEach { line ->
            if (htmlBuilder.isNotEmpty()) htmlBuilder.append("<br>")
            line.split(" ").dropLastWhile { it.isEmpty() }.forEach { word ->
                if (Patterns.WEB_URL.matcher(word).matches())
                    htmlBuilder.append("<a href=\"").append(word).append("\">").append(word).append("</a>")
                else if (word.isNotEmpty()) {
                    if (word[0] == '@' || word[0] == '.' && word[1] == '@') {
                        val index = word.indexOf('@')
                        var i = index + 1
                        while (i < word.length) {
                            if (isSpecialCHar(word[i])) {
                                endString = word.substring(i)
                                break
                            }
                            i++
                        }

                        val word2 = word.substring(index, i)
                        htmlBuilder.append(if (index == 0) "" else ".")
                                .append("<a href=\"com.andreapivetta.blu.user://")
                                .append(word2.substring(1))
                                .append("\">").append(word2).append("</a>")
                                .append(endString)
                    } else if (word[0] == '#') {
                        var word2 = word
                        for (i in 1..word.length - 1)
                            if (isSpecialCHar(word[i])) {
                                endString = word.substring(i)
                                word2 = word.substring(0, i)
                                break
                            }

                        htmlBuilder.append("<a href=\"com.andreapivetta.blu.hashtag://")
                                .append(word2.substring(1)).append("\">")
                                .append(word2)
                                .append("</a>").append(endString)
                    } else htmlBuilder.append(word)
                } else htmlBuilder.append(word)
                htmlBuilder.append(" ")
            }
        }

        return Html.fromHtml(htmlBuilder.toString())
    }

    private fun isSpecialCHar(char: Char) = "|/()=?'^[],;.:-\"\\".contains(char)
}