package com.andreapivetta.blu.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.ui.base.custom.ThemedActivity
import com.andreapivetta.blu.ui.explore.ExploreFragment
import com.andreapivetta.blu.ui.home.HomeFragment
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.notifications.NotificationsFragment
import com.andreapivetta.blu.ui.privatemessages.PrivateMessagesFragment
import com.andreapivetta.blu.ui.setup.SetupActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ThemedActivity(), MainMvpView {

    private val presenter: MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        presenter.attachView(this)

        bottomBar.setOnTabSelectListener { menuItemId ->
            when (menuItemId) {
                R.id.timeline -> {
                    setTitle(R.string.home)
                    fab.show()
                    pushFragment(HomeFragment.newInstance())
                }
                R.id.explore -> {
                    setTitle(R.string.explore)
                    fab.hide()
                    pushFragment(ExploreFragment.newInstance())
                }
                R.id.messages -> {
                    setTitle(R.string.messages)
                    fab.hide()
                    pushFragment(PrivateMessagesFragment.newInstance())
                }
                R.id.notifications -> {
                    setTitle(R.string.notifications)
                    fab.hide()
                    pushFragment(NotificationsFragment.newInstance())
                }
                else -> throw RuntimeException("Item not found")
            }
        }

        fab.setOnClickListener { presenter.fabClicked() }

        if (savedInstanceState == null)
            pushFragment(HomeFragment.newInstance())

        if (!AppSettingsFactory.getAppSettings(this).isUserDataDownloaded())
            SetupActivity.launch(this)
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_frameLayout, fragment).commit()
    }

    override fun newTweet() {
        NewTweetActivity.launch(this)
    }
}
