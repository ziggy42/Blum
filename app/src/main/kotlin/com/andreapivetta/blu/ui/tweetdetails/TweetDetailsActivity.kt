package com.andreapivetta.blu.ui.tweetdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.ThemedActivity

class TweetDetailsActivity : ThemedActivity() {

    companion object {
        fun launch(context: Context, id: Long, screenName: String) {
            val intent = Intent(context, TweetDetailsActivity::class.java)
            intent.putExtra(TweetDetailsFragment.TAG_ID, id)
                    .putExtra(TweetDetailsFragment.TAG_SCREEN_NAME, screenName)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_details)
        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container_frameLayout, TweetDetailsFragment.newInstance(
                            intent.getLongExtra(TweetDetailsFragment.TAG_ID, -1L))).commit()
    }

}
