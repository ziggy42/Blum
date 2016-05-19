package com.andreapivetta.blu.ui.timeline.holders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.andreapivetta.blu.R
import com.bumptech.glide.Glide

import java.util.ArrayList

import twitter4j.Status
import twitter4j.Twitter

class VHItemQuote(container: View) : VHItem(container) {

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

    override fun setup(status: Status, context: Context, favorites: MutableList<Long>,
                       retweets: MutableList<Long>, twitter: Twitter) {
        super.setup(status, context, favorites, retweets, twitter)

        val quotedStatus = status.quotedStatus
        if (quotedStatus != null) {
            quotedUserNameTextView.text = quotedStatus.user.name

            if (quotedStatus.mediaEntities.size > 0) {
                photoImageView.visibility = View.VISIBLE
                Glide.with(context).load(quotedStatus.mediaEntities[0].mediaURL).placeholder(R.drawable.placeholder).into(photoImageView)

                quotedStatusTextView.text = quotedStatus.text.replace(quotedStatus.mediaEntities[0].url, "")
            } else {
                photoImageView.visibility = View.GONE
                quotedStatusTextView.text = quotedStatus.text
            }

            quotedStatusLinearLayout.setOnClickListener { /* TODO */ }
        } else {
            quotedStatusLinearLayout.visibility = View.GONE
        }
    }
}
