package com.andreapivetta.blu.ui.tweetdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.pushFragment
import com.andreapivetta.blu.ui.custom.ThemedActivity
import kotlinx.android.synthetic.main.activity_tweet_details.*

class TweetDetailsActivity : ThemedActivity() {

    companion object {
        const val TAG_ID = "id"

        fun launch(context: Context, id: Long) {
            val intent = Intent(context, TweetDetailsActivity::class.java)
            intent.putExtra(TAG_ID, id)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null)
            pushFragment(R.id.container_frameLayout,
                    TweetDetailsFragment.newInstance(intent.getLongExtra(TAG_ID, -1L)))
    }

}
