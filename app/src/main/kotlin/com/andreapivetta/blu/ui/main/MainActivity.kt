package com.andreapivetta.blu.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.custom.ThemedActivity
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.settings.SettingsActivity
import com.andreapivetta.blu.ui.timeline.TimelineFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ThemedActivity(), MainMvpView {

    private val presenter: MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        presenter.attachView(this)

        fab.setOnClickListener { presenter.fabClicked() }

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container_frameLayout, TimelineFragment.newInstance()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_settings)
            openSettings()
        return super.onOptionsItemSelected(item)
    }

    override fun openSettings() {
        SettingsActivity.launch(this)
    }

    override fun openNotifications() {
        throw UnsupportedOperationException()
    }

    override fun openDirectMessages() {
        throw UnsupportedOperationException()
    }

    override fun newTweet() {
        NewTweetActivity.launch(this)
    }
}
