package com.andreapivetta.blu.ui.timeline

import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.Common
import com.andreapivetta.blu.data.twitter.TwitterUtils
import com.andreapivetta.blu.ui.base.decorators.SpaceTopItemDecoration
import twitter4j.Status

/**
 * Created by andrea on 17/05/16.
 */
class TimelineFragment : Fragment(), TimelineMvpView {

    companion object {
        fun newInstance(): TimelineFragment {
            return TimelineFragment()
        }
    }

    private val presenter: TimelinePresenter by lazy { TimelinePresenter() }

    private lateinit var adapter: TimelineAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_timeline, container, false)

        recyclerView = rootView?.findViewById(R.id.tweetsRecyclerView) as RecyclerView
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        loadingProgressBar = rootView?.findViewById(R.id.loadingProgressBar) as ProgressBar

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Common.dpToPx(activity, 10)))
        adapter = TimelineAdapter(activity, TwitterUtils.getTwitter(), -1)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener { presenter.onRefresh() }
        swipeRefreshLayout.setColorSchemeColors(getRefreshColor())

        presenter.getTweets()
        return rootView
    }

    @ColorRes private fun getRefreshColor(): Int {
        val typedValue = TypedValue()
        activity.theme.resolveAttribute(R.attr.appColorPrimary, typedValue, true)
        return typedValue.data
    }

    override fun showTweets(tweets: MutableList<Status>) {
        adapter.mDataSet = tweets
        adapter.notifyDataSetChanged()
    }

    override fun showTweet(tweet: Status) {
        adapter.mDataSet.add(0, tweet)
        adapter.mDataSet.removeAt(adapter.mDataSet.size - 1)
        recyclerView.scrollToPosition(0)
        adapter.notifyItemInserted(0)
    }

    override fun getLastTweetId(): Long {
        return adapter.mDataSet[0].id
    }

    override fun stopRefresh() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showEmpty() {
        // TODO
    }

    override fun showError() {
        // TODO
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT);
    }

    override fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

}