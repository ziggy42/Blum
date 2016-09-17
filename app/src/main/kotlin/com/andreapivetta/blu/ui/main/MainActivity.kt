package com.andreapivetta.blu.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.ui.base.custom.ThemedActivity
import com.andreapivetta.blu.ui.explore.ExploreFragment
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.notifications.NotificationsFragment
import com.andreapivetta.blu.ui.privatemessages.PrivateMessagesFragment
import com.andreapivetta.blu.ui.search.SearchActivity
import com.andreapivetta.blu.ui.settings.SettingsActivity
import com.andreapivetta.blu.ui.setup.SetupActivity
import com.andreapivetta.blu.ui.timeline.TimelineFragment
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
                    fab.show()
                    pushFragment(TimelineFragment.newInstance())
                }
                R.id.explore -> {
                    fab.hide()
                    pushFragment(ExploreFragment.newInstance())
                }
                R.id.messages -> {
                    fab.hide()
                    pushFragment(PrivateMessagesFragment.newInstance())
                }
                R.id.notifications -> {
                    fab.hide()
                    pushFragment(NotificationsFragment.newInstance())
                }
                else -> throw RuntimeException("Item not found")
            }
        }

        fab.setOnClickListener { presenter.fabClicked() }

        if (savedInstanceState == null)
            pushFragment(TimelineFragment.newInstance())

        if (!AppSettingsFactory.getAppSettings(this).isUserDataDownloaded())
            SetupActivity.launch(this)
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container_frameLayout, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView = MenuItemCompat.
                getActionView(menu?.findItem(R.id.action_search)) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                search(searchView.query.toString())
                return true
            }

            override fun onQueryTextChange(s: String) = false
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> openSettings()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun openSettings() {
        SettingsActivity.launch(this)
    }

    override fun search(string: String) {
        SearchActivity.launch(this, string)
    }

    override fun newTweet() {
        NewTweetActivity.launch(this)
    }
}
