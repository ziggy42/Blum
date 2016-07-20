package com.andreapivetta.blu.ui.timeline.holders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.data.twitter.model.Tweet
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.bumptech.glide.Glide

class StatusQuoteViewHolder(container: View, listener: InteractionListener) :
        StatusViewHolder(container, listener) {

    private val quotedUserNameTextView = container.findViewById(R.id.quotedUserNameTextView) as TextView
    private val quotedStatusTextView = container.findViewById(R.id.quotedStatusTextView) as TextView
    private val photoImageView = container.findViewById(R.id.photoImageView) as ImageView
    private val quotedStatusLinearLayout = container.findViewById(R.id.quotedStatus) as LinearLayout

    override fun setup(tweet: Tweet) {
        super.setup(tweet)

        if (tweet.quotedStatus) {
            val quotedStatus = tweet.getQuotedTweet()
            quotedUserNameTextView.text = quotedStatus.user.name

            if (quotedStatus.mediaEntities.size > 0) {
                photoImageView.visibility = View.VISIBLE
                Glide.with(container.context)
                        .load(quotedStatus.mediaEntities[0].mediaURL)
                        .placeholder(R.drawable.placeholder)
                        .into(photoImageView)

                quotedStatusTextView.text = quotedStatus.getTextWithoutMediaURLs()
            } else {
                photoImageView.visibility = View.GONE
                quotedStatusTextView.text = quotedStatus.text
            }

            quotedStatusLinearLayout.setOnClickListener {
                listener.openTweet(quotedStatus, quotedStatus.user)
            }
        } else quotedStatusLinearLayout.visibility = View.GONE
    }

}
