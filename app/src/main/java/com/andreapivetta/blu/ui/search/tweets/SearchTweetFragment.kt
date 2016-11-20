package com.andreapivetta.blu.ui.search.tweets

import android.os.Bundle
import com.andreapivetta.blu.ui.timeline.TimelineFragment

/**
 * Created by andrea on 25/07/16.
 */
class SearchTweetFragment : TimelineFragment() {

    companion object {
        private val TAG_QUERY = "query"

        fun newInstance(query: String): SearchTweetFragment {
            val fragment = SearchTweetFragment()
            val bundle = Bundle()
            bundle.putString(TAG_QUERY, query)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getTimelinePresenter() = SearchTweetPresenter(arguments.getString(TAG_QUERY))

}