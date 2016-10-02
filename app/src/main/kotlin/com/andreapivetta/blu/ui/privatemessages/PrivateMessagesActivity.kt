package com.andreapivetta.blu.ui.privatemessages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.custom.ThemedActivity
import kotlinx.android.synthetic.main.activity_newtweet.*

class PrivateMessagesActivity : ThemedActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, PrivateMessagesActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_messages)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(R.id.container_frameLayout,
                    PrivateMessagesFragment.newInstance()).commit()
    }
}
