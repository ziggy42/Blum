package com.andreapivetta.blu.ui.profile.viewholders

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadAvatar
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.common.utils.openUrl
import com.andreapivetta.blu.ui.hashtag.HashtagActivity
import com.andreapivetta.blu.ui.profile.UserActivity
import com.luseen.autolinklibrary.AutoLinkMode
import kotlinx.android.synthetic.main.profile.view.*
import twitter4j.User

/**
 * Created by andrea on 14/11/16.
 */
class ProfileViewHolder(container: View) : RecyclerView.ViewHolder(container) {

    private val bannerImageView = container.bannerImageView
    private val picImageView = container.picImageView
    private val nameTextView = container.nameTextView
    private val screenNameTextView = container.screenNameTextView
    private val descriptionTextView = container.descriptionTextView
    private val extraTextView = container.extraTextView
    private val statsTextView = container.statsTextView
    private val context: Context = container.context

    fun setup(user: User) {
        bannerImageView.loadUrl(user.profileBannerRetinaURL)
        picImageView.loadAvatar(user.biggerProfileImageURLHttps)
        nameTextView.text = user.name
        screenNameTextView.text = "@${user.screenName}"
        statsTextView.text = getStats(user)

        descriptionTextView.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_URL,
                AutoLinkMode.MODE_MENTION)
        descriptionTextView.setHashtagModeColor(ContextCompat
                .getColor(context, R.color.blueThemeColorAccent))
        descriptionTextView.setUrlModeColor(ContextCompat
                .getColor(context, R.color.blueThemeColorAccent))
        descriptionTextView.setMentionModeColor(ContextCompat
                .getColor(context, R.color.blueThemeColorAccent))
        descriptionTextView.setAutoLinkText(user.description)
        descriptionTextView.setAutoLinkOnClickListener { mode, text ->
            when (mode) {
                AutoLinkMode.MODE_HASHTAG -> HashtagActivity.launch(context, text)
                AutoLinkMode.MODE_MENTION -> UserActivity.launch(context, text)
                AutoLinkMode.MODE_URL -> openUrl(context as Activity, text)
                else -> throw UnsupportedOperationException("No handlers for mode $mode")
            }
        }

        extraTextView.addAutoLinkMode(AutoLinkMode.MODE_URL)
        extraTextView.setUrlModeColor(ContextCompat.getColor(context, R.color.blueThemeColorAccent))
        extraTextView.setAutoLinkText(getExtra(user))
        extraTextView.setAutoLinkOnClickListener { mode, text ->
            when (mode) {
                AutoLinkMode.MODE_URL -> openUrl(context as Activity, text)
                else -> throw UnsupportedOperationException("No handlers for mode $mode")
            }
        }

        if (user.isVerified)
            nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified_user, 0)
    }

    private fun getExtra(user: User): String = if (user.url.isNullOrEmpty()) user.location else {
        if (user.location.isNullOrEmpty()) user.url else
            "${user.location} • ${user.url}"
    }

    private fun getStats(user: User): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        val followersString = getCount(user.followersCount)
        val followersSpan = SpannableString(followersString)
        followersSpan.setSpan(StyleSpan(Typeface.BOLD), 0, followersString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val followingString = getCount(user.friendsCount)
        val followingSpan = SpannableString(followingString)
        followingSpan.setSpan(StyleSpan(Typeface.BOLD), 0, followingString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder.append(followersSpan)
                .append(" followers ")
                .append(followingSpan)
                .append(" following")
        return builder
    }

    private fun getCount(amount: Int) = if (amount < 10000) amount.toString() else
        (amount / 1000).toString() + "k"
}