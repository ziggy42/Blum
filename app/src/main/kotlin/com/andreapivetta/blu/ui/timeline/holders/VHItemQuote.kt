package com.andreapivetta.blu.ui.timeline.holders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.timeline.InteractionListener
import com.andreapivetta.blu.ui.timeline.TweetInfoProvider
import com.bumptech.glide.Glide
import twitter4j.Status

class VHItemQuote(container: View, listener: InteractionListener, tweetInfoProvider: TweetInfoProvider) :
        VHItem(container, listener, tweetInfoProvider) {

    private val quotedUserNameTextView: TextView
    private val quotedStatusTextView: TextView
    private val photoImageView: ImageView
    private val quotedStatusLinearLayout: LinearLayout

    init {
        this.quotedUserNameTextView = container.findViewById(R.id.quotedUserNameTextView) as TextView
        this.quotedStatusTextView = container.findViewById(R.id.quotedStatusTextView) as TextView
        this.photoImageView = container.findViewById(R.id.photoImageView) as ImageView
        this.quotedStatusLinearLayout = container.findViewById(R.id.quotedStatus) as LinearLayout
    }

    override fun setup(status: Status) {
        super.setup(status)

        val quotedStatus = status.quotedStatus
        if (quotedStatus != null) {
            quotedUserNameTextView.text = quotedStatus.user.name

            if (quotedStatus.mediaEntities.size > 0) {
                photoImageView.visibility = View.VISIBLE
                Glide.with(container.context)
                        .load(quotedStatus.mediaEntities[0].mediaURL)
                        .placeholder(R.drawable.placeholder)
                        .into(photoImageView)

                quotedStatusTextView.text = quotedStatus.text.replace(quotedStatus.mediaEntities[0].url, "")
            } else {
                photoImageView.visibility = View.GONE
                quotedStatusTextView.text = quotedStatus.text
            }

            quotedStatusLinearLayout.setOnClickListener { listener.openTweet(quotedStatus) }
        } else quotedStatusLinearLayout.visibility = View.GONE
    }
}
