package com.andreapivetta.blu.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.pushFragment
import com.andreapivetta.blu.ui.base.custom.ThemedActivity
import kotlinx.android.synthetic.main.activity_newtweet.*

class NotificationsActivity : ThemedActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, NotificationsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null)
            pushFragment(R.id.container_frameLayout, NotificationsFragment.newInstance())
    }
}
