package com.andreapivetta.blu.ui.timeline

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.custom.Theme
import com.andreapivetta.blu.ui.custom.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.image.ImageActivity
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.profile.UserActivity
import com.andreapivetta.blu.ui.tweetdetails.TweetDetailsActivity
import com.andreapivetta.blu.ui.video.VideoActivity
import twitter4j.User
import java.io.Serializable
import java.util.*

/**
 * Created by andrea on 17/05/16.
 */
open class TimelineFragment : Fragment(), TimelineMvpView, InteractionListener {

    companion object {
        fun newInstance() = TimelineFragment()
    }

    private val TAG_TWEET_LIST = "tweet_list"
    private val TAG_PAGE = "page"

    private val presenter: TimelinePresenter by lazy { getTimelinePresenter() }

    private lateinit var adapter: TimelineAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var badThingsViewGroup: ViewGroup

    protected open fun getTimelinePresenter() = TimelinePresenter()

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        presenter.attachView(this)

        adapter = TimelineAdapter(this)
        if (bundle != null) {
            adapter.tweets = bundle.getSerializable(TAG_TWEET_LIST) as MutableList<Tweet>
            presenter.page = bundle.getInt(TAG_PAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_timeline, container, false)

        recyclerView = rootView?.findViewById(R.id.tweetsRecyclerView) as RecyclerView
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        loadingProgressBar = rootView.findViewById(R.id.loadingProgressBar) as ProgressBar
        badThingsViewGroup = rootView.findViewById(R.id.badThingsViewGroup) as ViewGroup

        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Utils.dpToPx(activity, 10)))
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (linearLayoutManager.childCount + linearLayoutManager
                        .findFirstVisibleItemPosition() + 1 > linearLayoutManager.itemCount - 10)
                    presenter.getMoreTweets()
            }
        })

        swipeRefreshLayout.setOnRefreshListener { presenter.onRefresh() }
        swipeRefreshLayout.setColorSchemeColors(Theme.getColorPrimary(context))

        rootView.findViewById(R.id.retryButton)?.setOnClickListener {
            badThingsViewGroup.visible(false)
            presenter.getTweets()
        }

        if (adapter.tweets.isEmpty())
            presenter.getTweets()
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(TAG_TWEET_LIST, if (adapter.tweets.size > 30)
            ArrayList(adapter.tweets.subList(0, 30)) else adapter.tweets as Serializable)
        outState?.putInt(TAG_PAGE, presenter.page)
        super.onSaveInstanceState(outState)
    }

    // TimelineMvpView

    override fun showTweets(tweets: MutableList<Tweet>) {
        adapter.tweets = tweets
        adapter.notifyDataSetChanged()
    }

    override fun showTweet(tweet: Tweet) {
        val removedPosition = adapter.tweets.size - 1
        adapter.tweets.removeAt(removedPosition)
        adapter.notifyItemRemoved(removedPosition)

        adapter.tweets.add(0, tweet)
        adapter.notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
    }

    override fun showMoreTweets(tweets: MutableList<Tweet>) {
        adapter.tweets.addAll(tweets)
    }

    override fun getLastTweetId(): Long = if (adapter.tweets.size > 0) adapter.tweets[0].id else -1

    override fun stopRefresh() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showEmpty() {
        badThingsViewGroup.visible()
    }

    override fun showError() {
        badThingsViewGroup.visible()
    }

    override fun showSnackBar(stringResource: Int) {
        Snackbar.make(view!!, getString(stringResource), Snackbar.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        loadingProgressBar.visible()
    }

    override fun hideLoading() {
        loadingProgressBar.visible(false)
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
                .setNeutralButton(context.getString(R.string.quote),
                        { dialogInterface, i -> NewTweetActivity.launch(context, tweet) })
                .setNegativeButton(R.string.cancel, null)
                .create().show()
    }

    override fun unfavorite(tweet: Tweet) {
        presenter.unfavorite(tweet)
    }

    override fun unretweet(tweet: Tweet) {
        presenter.unretweet(tweet)
    }

    override fun reply(tweet: Tweet, user: User) {
        presenter.reply(tweet, user)
    }

    override fun openTweet(tweet: Tweet) {
        TweetDetailsActivity.launch(context, tweet.id)
    }

    override fun showUser(user: User) {
        UserActivity.launch(context, user)
    }

    override fun showImage(imageUrl: String) {
        ImageActivity.launch(context, arrayOf(imageUrl))
    }

    override fun showImages(imageUrls: List<String>, index: Int) {
        ImageActivity.launch(context, imageUrls.toTypedArray(), index)
    }

    override fun showVideo(videoUrl: String, videoType: String) {
        VideoActivity.launch(context, videoUrl, videoType)
    }

}