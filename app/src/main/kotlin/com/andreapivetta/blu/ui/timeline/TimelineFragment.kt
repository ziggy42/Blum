package com.andreapivetta.blu.ui.timeline

import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.Common
import com.andreapivetta.blu.data.twitter.model.Tweet
import com.andreapivetta.blu.ui.base.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.image.ImageActivity
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.profile.ProfileActivity
import com.andreapivetta.blu.ui.tweetdetails.TweetDetailsActivity
import twitter4j.User
import java.io.Serializable

/**
 * Created by andrea on 17/05/16.
 */
open class TimelineFragment : Fragment(), TimelineMvpView, InteractionListener {

    companion object {
        fun newInstance() = TimelineFragment()

        val TAG_TWEET_LIST = "tweet_list"
        val TAG_PAGE = "page"
    }

    private val presenter: TimelinePresenter by lazy { getTimelinePresenter() }

    private lateinit var adapter: TimelineAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var loadingProgressBar: ProgressBar

    protected open fun getTimelinePresenter() = TimelinePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)

        adapter = TimelineAdapter(this)
        if (savedInstanceState != null) {
            adapter.mDataSet = savedInstanceState.getSerializable(TAG_TWEET_LIST) as MutableList<Tweet>
            presenter.page = savedInstanceState.getInt(TAG_PAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_timeline, container, false)

        recyclerView = rootView?.findViewById(R.id.tweetsRecyclerView) as RecyclerView
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        loadingProgressBar = rootView?.findViewById(R.id.loadingProgressBar) as ProgressBar

        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Common.dpToPx(activity, 10)))
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (linearLayoutManager.childCount + linearLayoutManager.findFirstVisibleItemPosition() + 1 >
                        linearLayoutManager.itemCount - 10)
                    presenter.getMoreTweets()
            }
        })

        swipeRefreshLayout.setOnRefreshListener { presenter.onRefresh() }
        swipeRefreshLayout.setColorSchemeColors(getRefreshColor())

        if (adapter.mDataSet.isEmpty())
            presenter.getTweets()
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(TAG_TWEET_LIST, adapter.mDataSet as Serializable)
        outState?.putInt(TAG_PAGE, presenter.page)
        super.onSaveInstanceState(outState)
    }

    @ColorRes private fun getRefreshColor(): Int {
        val typedValue = TypedValue()
        activity.theme.resolveAttribute(R.attr.appColorPrimary, typedValue, true)
        return typedValue.data
    }

    // TimelineMvpView

    override fun showTweets(tweets: MutableList<Tweet>) {
        adapter.mDataSet = tweets
        adapter.notifyDataSetChanged()
    }

    override fun showTweet(tweet: Tweet) {
        adapter.mDataSet.add(0, tweet)
        adapter.mDataSet.removeAt(adapter.mDataSet.size - 1)
        recyclerView.scrollToPosition(0)
        adapter.notifyItemInserted(0)
    }

    override fun showMoreTweets(tweets: MutableList<Tweet>) {
        adapter.mDataSet.addAll(tweets)
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
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT)
    }

    override fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun showNewTweet(tweet: Tweet, user: User) {
        NewTweetActivity.launch(context, "@${user.screenName}", tweet.id)
    }

    override fun updateRecyclerViewView() {
        adapter.notifyDataSetChanged()
    }

    // InteractionListener

    override fun favorite(tweet: Tweet) {
        presenter.favorite(tweet)
    }

    override fun retweet(tweet: Tweet) {
        AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.retweet_title))
                .setPositiveButton(context.getString(R.string.retweet),
                        { dialogInterface, i -> presenter.retweet(tweet) })
                .setNegativeButton(R.string.cancel, null)
                .create().show()
    }

    override fun unfavorite(tweet: Tweet) {
        presenter.unfavorite(tweet)
    }

    override fun unretweet(tweet: Tweet) {
        // TODO
        Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
    }

    override fun reply(tweet: Tweet, user: User) {
        presenter.reply(tweet, user)
    }

    override fun openTweet(tweet: Tweet, user: User) {
        TweetDetailsActivity.launch(context, tweet.id, user.screenName)
    }

    override fun showUser(user: User) {
        ProfileActivity.launch(context, user)
    }

    override fun showImage(imageUrl: String) {
        ImageActivity.launch(context, arrayOf(imageUrl))
    }

    override fun showImages(imageUrls: List<String>, index: Int) {
        ImageActivity.launch(context, imageUrls.toTypedArray())
    }

    override fun showVideo(videoUrl: String, videoType: String) {
        // TODO
    }

}