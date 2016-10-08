package com.andreapivetta.blu.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.text.util.Linkify
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.ui.mediatimeline.MediaFragment
import com.andreapivetta.blu.ui.usertimeline.UserTimelineFragment
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.profile_header.*
import twitter4j.User

class ProfileActivity : AppCompatActivity() {

    private var user: User? = null

    companion object {
        fun launch(context: Context, user: User) {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(TAG_USER, user)
            context.startActivity(intent)
        }

        val TAG_USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        user = intent.getSerializableExtra(TAG_USER) as User

        viewPager.adapter = SimpleAdapter()
        tabLayout.setupWithViewPager(viewPager)
        setupView()
    }

    private fun setupView() {
        header.loadUrl(user!!.profileBackgroundImageUrlHttps)
        userProfilePicImageView.loadUrl(user!!.profileImageURLHttps)
        userNameTextView.text = user?.name
        userNickTextView.text = "@${user?.screenName}"
        statsTextView.text = "${user?.friendsCount.toString()} followings " +
                "${user?.followersCount.toString()} followers"
        descriptionTextView.text = user?.description

        Linkify.addLinks(descriptionTextView, Linkify.ALL)

        if (user?.location != null) userLocationTextView.text = user?.location
        else userLocationTextView.visible(false)

        if (user?.url != null) userWebsiteTextView.text = user?.url
        else userWebsiteTextView.visible(false)

        if (user != null && user!!.isVerified)
            userNameTextView.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0, R.drawable.ic_verified_user, 0)
    }

    inner class SimpleAdapter : FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return UserTimelineFragment.newInstance(user!!.id)
                1 -> return MediaFragment.newInstance(user!!.id)
            }

            throw UnsupportedOperationException()
        }

        override fun getCount() = this@ProfileActivity.resources.getStringArray(R.array.user_profile_tabs).size

        override fun getPageTitle(position: Int): String =
                this@ProfileActivity.resources.getStringArray(R.array.user_profile_tabs)[position]
    }
}
