package com.andreapivetta.blu.ui.tweetdetails

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
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
import com.andreapivetta.blu.data.twitter.getTweetLink
import com.andreapivetta.blu.ui.custom.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.image.ImageActivity
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.profile.UserActivity
import com.andreapivetta.blu.ui.video.VideoActivity
import twitter4j.User
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class TweetDetailsFragment : Fragment(), TweetDetailsMvpView, DetailsInteractionListener {

    companion object {
        val TAG_ID = "id"
        val TAG_SCREEN_NAME = "screen_name"
        val TAG_TWEET_LIST = "tweet_list"
        val TAG_INDEX = "index"

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
    private lateinit var badThingsViewGroup: ViewGroup
    private lateinit var adapter: SingleTweetAdapter

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusId = arguments.getLong(TAG_ID)

        presenter.attachView(this)

        adapter = SingleTweetAdapter(this)
        if (savedInstanceState != null) {
            adapter.headerIndex = savedInstanceState.getInt(TAG_INDEX)
            adapter.mDataSet = savedInstanceState
                    .getSerializable(TAG_TWEET_LIST) as MutableList<Tweet>
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_tweet_details, container, false)

        recyclerView = rootView?.findViewById(R.id.tweetsRecyclerView) as RecyclerView
        badThingsViewGroup = rootView?.findViewById(R.id.badThingsViewGroup) as ViewGroup
        loadingProgressBar = rootView?.findViewById(R.id.loadingProgressBar) as ProgressBar

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Utils.dpToPx(activity, 10)))
        recyclerView.adapter = adapter

        rootView?.findViewById(R.id.retryButton)?.setOnClickListener {
            badThingsViewGroup.visible(false)
            presenter.getConversation(statusId)
        }

        if (adapter.mDataSet.isEmpty())
            presenter.getConversation(statusId)
        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(TAG_TWEET_LIST, ArrayList(adapter.mDataSet))
        outState?.putInt(TAG_INDEX, adapter.headerIndex)
        super.onSaveInstanceState(outState)
    }

    override fun showTweets(headerIndex: Int, tweets: MutableList<Tweet>) {
        adapter.headerIndex = headerIndex
        adapter.mDataSet = tweets
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(headerIndex)
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

    override fun openTweet(tweet: Tweet, user: User) {
        TweetDetailsActivity.launch(context, tweet.id, user.screenName)
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

    override fun shareTweet(tweet: Tweet) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, getTweetLink(tweet))
        intent.type = "text/plain"
        startActivity(intent)
    }

    override fun quoteTweet(tweet: Tweet) {
        NewTweetActivity.launch(context, tweet)
    }
}
