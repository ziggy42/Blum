package com.andreapivetta.blu.ui.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.MediaController
import com.andreapivetta.blu.R
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() {

    companion object {
        val TAG_URL = "video"
        val TAG_TYPE = "type"

        fun launch(context: Context, videoUrl: String, videoType: String) {
            val intent = Intent(context, VideoActivity::class.java)
            intent.putExtra(TAG_URL, videoUrl)
            intent.putExtra(TAG_TYPE, videoType)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val mc = MediaController(this)
        mc.setAnchorView(videoView)
        mc.setMediaPlayer(videoView)

        videoView.setOnPreparedListener { loadingProgressBar.visibility = View.GONE }
        if ("animated_gif" == intent.getStringExtra(TAG_TYPE))
            videoView.setOnCompletionListener { videoView.start() }

        videoView.setMediaController(mc)
        videoView.setVideoURI(Uri.parse(intent.getStringExtra(TAG_URL)))
        videoView.requestFocus()
        videoView.start()
    }

}
