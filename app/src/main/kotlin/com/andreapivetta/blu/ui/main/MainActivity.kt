package com.andreapivetta.blu.ui.main

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.ui.base.custom.ThemedActivity
import com.andreapivetta.blu.ui.explore.ExploreFragment
import com.andreapivetta.blu.ui.home.HomeFragment
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.notifications.NotificationsFragment
import com.andreapivetta.blu.ui.privatemessages.PrivateMessagesFragment
import com.andreapivetta.blu.ui.setup.SetupActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : ThemedActivity(), MainMvpView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        bottomBar.setOnTabSelectListener { menuItemId ->
            when (menuItemId) {
                R.id.timeline -> {
                    setTitle(R.string.home)
                    showFab(R.drawable.ic_create)
                    fab.setOnClickListener { newTweet() }
                    pushFragment(HomeFragment.newInstance())
                }
                R.id.explore -> {
                    setTitle(R.string.explore)
                    hideFab()
                    pushFragment(ExploreFragment.newInstance())
                }
                R.id.messages -> {
                    setTitle(R.string.messages)
                    showFab(R.drawable.ic_message)
                    fab.setOnClickListener { newConversation() }
                    pushFragment(PrivateMessagesFragment.newInstance())
                }
                R.id.notifications -> {
                    setTitle(R.string.notifications)
                    hideFab()
                    pushFragment(NotificationsFragment.newInstance())
                }
                else -> throw RuntimeException("Item not found")
            }
        }

        if (savedInstanceState == null)
            pushFragment(HomeFragment.newInstance())

        if (!AppSettingsFactory.getAppSettings(this).isUserDataDownloaded())
            SetupActivity.launch(this)
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_frameLayout, fragment).commit()
    }

    private fun showFab(@DrawableRes icon: Int) {
        fab.setImageResource(icon)
        fab.visible()
        fab.show()
    }

    private fun hideFab() {
        fab.visible(false)
    }

    override fun newTweet() {
        NewTweetActivity.launch(this)
    }

    override fun newConversation() {
        // TODO
        Timber.i("New conversation")
    }
}
