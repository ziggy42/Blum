package com.andreapivetta.blu.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.custom.ThemedActivity
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : ThemedActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null)
            fragmentManager.beginTransaction()
                    .add(R.id.container_frameLayout, SettingsFragment()).commit()
    }

}
