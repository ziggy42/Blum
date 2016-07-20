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
import com.andreapivetta.blu.data.twitter.model.Tweet
import com.andreapivetta.blu.ui.base.decorators.SpaceLeftItemDecoration
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.holders.BaseViewHolder
import com.andreapivetta.blu.ui.timeline.holders.ImagesAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

/**
 * Created by andrea on 26/05/16.
 */
class StatusDetailsViewHolder(container: View, listener: InteractionListener) :
        BaseViewHolder(container, listener) {

    private val mediaViewStub = container.findViewById(R.id.mediaViewStub) as ViewStub
    private val quotedStatusViewStub = container.findViewById(R.id.quotedStatusViewStub) as ViewStub

    private var inflatedMediaView: View? = null
    private var inflatedQuotedView: View? = null

    override fun setup(tweet: Tweet) {
        val currentUser = tweet.user

        userNameTextView.text = currentUser.name
        timeTextView.text = formatDate(tweet.timeStamp, container.context)
        statusTextView.text = getHtmlStatusText(tweet)
        statusTextView.movementMethod = LinkMovementMethod.getInstance()

        userScreenNameTextView.text = "@" + currentUser.screenName

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

        Glide.with(container.context).load(currentUser.biggerProfileImageURL)
                .dontAnimate().placeholder(R.drawable.placeholder).into(userProfilePicImageView)

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

        if (tweet.hasSingleImage()) {
            if (inflatedMediaView == null) {
                mediaViewStub.layoutResource = R.layout.stub_photo
                inflatedMediaView = mediaViewStub.inflate()
            }

            Glide.with(container.context).load(tweet.mediaEntities[0].mediaURL)
                    .asBitmap().dontTransform()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .into(inflatedMediaView as ImageView)
            inflatedMediaView?.setOnClickListener { listener.showImage(tweet.mediaEntities[0].mediaURL) }
        } else if (tweet.hasMultipleMedia()) {
            if (inflatedMediaView == null) {
                mediaViewStub.layoutResource = R.layout.stub_photos
                inflatedMediaView = mediaViewStub.inflate()
            }

            val recyclerView = inflatedMediaView as RecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(SpaceLeftItemDecoration(5))
            recyclerView.adapter = ImagesAdapter(tweet.mediaEntities, listener)
            recyclerView.layoutManager = LinearLayoutManager(container.context, LinearLayoutManager.HORIZONTAL, false)
        } else if (tweet.hasSingleVideo()) {
            if (inflatedMediaView == null) {
                mediaViewStub.layoutResource = R.layout.video_cover
                inflatedMediaView = mediaViewStub.inflate()
            }

            Glide.with(container.context).load(tweet.mediaEntities[0].mediaURL)
                    .asBitmap().dontTransform()
                    .placeholder(R.drawable.placeholder)
                    .centerCrop().into(inflatedMediaView?.findViewById(R.id.tweetVideoImageView) as ImageView)

            inflatedMediaView?.findViewById(R.id.playVideoImageButton)?.setOnClickListener {
                listener.showVideo(tweet.mediaEntities[0].videoVariants[0].url,
                        tweet.mediaEntities[0].type)
            }
        }

        if (tweet.quotedStatus) {
            if (inflatedQuotedView == null) {
                quotedStatusViewStub.layoutResource = R.layout.quoted_tweet
                inflatedQuotedView = quotedStatusViewStub.inflate()
            }

            val quotedStatus = tweet.getQuotedTweet()

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

    private fun getHtmlStatusText(tweet: Tweet): Spanned {
        val text = tweet.text
        tweet.mediaEntities.forEach { media -> text.replace(media.url, "") }
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