package com.andreapivetta.blu.ui.main

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.timeline.TimelineFragment

class MainActivity : AppCompatActivity(), MainMvpView {

    private val presenter: MainPresenter = MainPresenter()

    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) as Toolbar }
    private val fab: FloatingActionButton by lazy { findViewById(R.id.fab) as FloatingActionButton }

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

    override fun openSettings() {
        throw UnsupportedOperationException()
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
