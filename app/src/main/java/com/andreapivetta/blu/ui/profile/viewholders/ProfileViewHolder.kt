package com.andreapivetta.blu.ui.profile.viewholders

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import com.andreapivetta.blu.common.utils.loadAvatar
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.ui.profile.UserMvpView
import kotlinx.android.synthetic.main.profile.view.*
import twitter4j.User

/**
 * Created by andrea on 14/11/16.
 */
class ProfileViewHolder(container: View, val userMvpView: UserMvpView) :
        RecyclerView.ViewHolder(container) {

    private val bannerImageView = container.bannerImageView
    private val picImageView = container.picImageView
    private val nameTextView = container.nameTextView
    private val screenNameTextView = container.screenNameTextView
    private val descriptionTextView = container.descriptionTextView
    private val extraTextView = container.extraTextView
    private val statsTextView = container.statsTextView

    fun setup(user: User) {
        bannerImageView.loadUrl(user.profileBannerRetinaURL)
        picImageView.loadAvatar(user.biggerProfileImageURLHttps)
        nameTextView.text = user.name
        screenNameTextView.text = user.screenName
        descriptionTextView.text = user.description
        extraTextView.text = getExtra(user)
        statsTextView.text = getStats(user)
    }

    private fun getExtra(user: User): CharSequence {
        if (!user.location.isNullOrEmpty()) {
            if (!user.url.isNullOrEmpty())
                return "${user.location} • ${user.url}"
            return user.location
        }
        return user.url
    }

    private fun getStats(user: User): CharSequence {
        val builder = SpannableStringBuilder()

        val followersString = "${user.followersCount}"
        val followersSpan = SpannableString(followersString)
        followersSpan.setSpan(StyleSpan(Typeface.BOLD), 0, followersString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val followingString = "${user.friendsCount}"
        val followingSpan = SpannableString(followingString)
        followersSpan.setSpan(StyleSpan(Typeface.BOLD), 0, followingString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder.append(followersSpan)
                .append(" followers ")
                .append(followingSpan)
                .append(" following")
        return builder.toString()
    }

}