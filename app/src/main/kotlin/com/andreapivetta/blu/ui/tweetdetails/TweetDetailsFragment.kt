package com.andreapivetta.blu.ui.tweetdetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.Common
import com.andreapivetta.blu.ui.base.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.timeline.InteractionListener
import twitter4j.Status
import twitter4j.User

/**
 * A placeholder fragment containing a simple view.
 */
class TweetDetailsFragment : Fragment(), TweetDetailsMvpView, InteractionListener {

    companion object {
        val TAG_ID = "id"
        val TAG_SCREEN_NAME = "screen_name"

        fun newInstance(id: Long): TweetDetailsFragment {
            val fragment = TweetDetailsFragment()
            val bundle = Bundle()
            bundle.putLong(TAG_ID, id)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val presenter: TweetDetailsPresenter by lazy { TweetDetailsPresenter() }

    private var statusId: Long = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var adapter: SingleTweetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusId = arguments.getLong(TAG_ID)

        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_tweet_details, container, false)

        recyclerView = rootView?.findViewById(R.id.tweetsRecyclerView) as RecyclerView
        loadingProgressBar = rootView?.findViewById(R.id.loadingProgressBar) as ProgressBar

        adapter = SingleTweetAdapter(this) // Move me away when savedInstanceStates occours
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Common.dpToPx(activity, 10)))
        recyclerView.adapter = adapter

        presenter.getConversation(statusId)
        return rootView
    }

    override fun showTweets(tweets: MutableList<Status>) {
        adapter.mDataSet = tweets
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(adapter.currentIndex)
    }

    override fun showTweet(tweet: Status) {
        throw UnsupportedOperationException()
    }

    override fun showMoreTweets(tweets: MutableList<Status>) {
        throw UnsupportedOperationException()
    }

    override fun getLastTweetId(): Long {
        throw UnsupportedOperationException()
    }

    override fun stopRefresh() {
        throw UnsupportedOperationException()
    }

    override fun showEmpty() {
        throw UnsupportedOperationException()
    }

    override fun showError() {
        throw UnsupportedOperationException()
    }

    override fun showSnackBar(message: String) {
        throw UnsupportedOperationException()
    }

    override fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showNewTweet(status: Status, user: User) {
        throw UnsupportedOperationException()
    }

    override fun favoriteAdded(status: Status) {
        throw UnsupportedOperationException()
    }

    override fun favoriteRemoved(status: Status) {
        throw UnsupportedOperationException()
    }

    override fun retweetAdded(status: Status) {
        throw UnsupportedOperationException()
    }

    override fun retweetRemoved(status: Status) {
        throw UnsupportedOperationException()
    }

    override fun setHeaderIndex(index: Int) {
        adapter.currentIndex = index
    }

    // InteractionListener

    override fun favorite(status: Status) {
        throw UnsupportedOperationException()
    }

    override fun retweet(status: Status) {
        throw UnsupportedOperationException()
    }

    override fun unfavorite(status: Status) {
        throw UnsupportedOperationException()
    }

    override fun unretweet(status: Status) {
        throw UnsupportedOperationException()
    }

    override fun reply(status: Status, user: User) {
        throw UnsupportedOperationException()
    }

    override fun openTweet(status: Status, user: User) {
        throw UnsupportedOperationException()
    }

    override fun showUser(user: User) {
        throw UnsupportedOperationException()
    }

    override fun showImage(imageUrl: String) {
        throw UnsupportedOperationException()
    }

    override fun showImages(imageUrls: List<String>, index: Int) {
        throw UnsupportedOperationException()
    }

    override fun showVideo(videoUrl: String, videoType: String) {
        throw UnsupportedOperationException()
    }
}
