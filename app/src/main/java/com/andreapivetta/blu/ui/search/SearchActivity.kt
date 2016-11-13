package com.andreapivetta.blu.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.base.custom.ThemedActivity
import com.andreapivetta.blu.ui.search.tweets.SearchTweetFragment
import com.andreapivetta.blu.ui.search.users.SearchUsersFragment
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : ThemedActivity() {

    companion object {
        private val ARG_QUERY = "query"

        fun launch(context: Context, query: String) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(ARG_QUERY, query)
            context.startActivity(intent)
        }
    }

    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        query = intent.getStringExtra(ARG_QUERY)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
        title = query

        viewPager.adapter = SearchFragmentPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    inner class SearchFragmentPagerAdapter(supportFragmentManager: FragmentManager) :
            FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> SearchTweetFragment.newInstance(query)
            1 -> SearchUsersFragment.newInstance(query)
            else -> throw RuntimeException("Unknown item")
        }

        override fun getPageTitle(position: Int): CharSequence = resources.
                getStringArray(R.array.search_tabs_names)[position]

        override fun getCount() = 2

    }

}
