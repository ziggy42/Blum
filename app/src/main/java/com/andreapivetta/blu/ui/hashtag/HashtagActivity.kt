package com.andreapivetta.blu.ui.hashtag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.pushFragment
import com.andreapivetta.blu.ui.custom.ThemedActivity
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.search.tweets.SearchTweetFragment
import kotlinx.android.synthetic.main.activity_main.*

class HashtagActivity : ThemedActivity() {

    companion object {
        val TAG_HASHTAG = "hashtag"

        fun launch(context: Context, hashtag: String) {
            val intent = Intent(context, HashtagActivity::class.java)
            intent.putExtra(TAG_HASHTAG, hashtag)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val hashtag = intent.getStringExtra(TAG_HASHTAG)
        title = hashtag

        fab.setOnClickListener { NewTweetActivity.launch(this, hashtag) }

        if (savedInstanceState == null)
            pushFragment(R.id.container_frameLayout, SearchTweetFragment.newInstance(hashtag))
    }

}
