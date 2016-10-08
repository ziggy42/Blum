package com.andreapivetta.blu.ui.timeline.holders

import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.quoted_tweet.view.*
import kotlinx.android.synthetic.main.tweet_quote.view.*

class StatusQuoteViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val quotedUserNameTextView = container.quotedUserNameTextView
    private val quotedStatusTextView = container.quotedStatusTextView
    private val photoImageView = container.photoImageView
    private val quotedStatusLinearLayout = container.quotedStatus

    override fun setup(tweet: Tweet) {
        super.setup(tweet)

        if (tweet.quotedStatus) {
            val quotedStatus = tweet.getQuotedTweet()
            quotedUserNameTextView.text = quotedStatus.user.name

            if (quotedStatus.mediaEntities.size > 0) {
                photoImageView.visible()
                Glide.with(container.context)
                        .load(quotedStatus.mediaEntities[0].mediaURL)
                        .placeholder(R.drawable.placeholder)
                        .into(photoImageView)

                quotedStatusTextView.text = quotedStatus.getTextWithoutMediaURLs()
            } else {
                photoImageView.visible(false)
                quotedStatusTextView.text = quotedStatus.text
            }

            quotedStatusLinearLayout.setOnClickListener {
                listener.openTweet(quotedStatus, quotedStatus.user)
            }
        } else quotedStatusLinearLayout.visible(false)
    }

}
