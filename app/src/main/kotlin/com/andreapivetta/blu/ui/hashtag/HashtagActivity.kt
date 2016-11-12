package com.andreapivetta.blu.ui.hashtag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.andreapivetta.blu.R

class HashtagActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_hashtag)
    }

    // TODO make this class great again
}
