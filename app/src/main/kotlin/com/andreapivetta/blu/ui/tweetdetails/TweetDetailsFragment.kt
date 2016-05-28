package com.andreapivetta.blu.ui.tweetdetails

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
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.Common
import com.andreapivetta.blu.ui.base.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
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

    override fun showNewTweet(status: Status, user: User) {
        NewTweetActivity.launch(context, "@" + user.screenName, status.id)
    }

    override fun favoriteAdded(status: Status) {
        adapter.setFavorite(status.id)
        adapter.notifyDataSetChanged()
    }

    override fun favoriteRemoved(status: Status) {
        adapter.removeFavorite(status.id)
        adapter.notifyDataSetChanged()
    }

    override fun retweetAdded(status: Status) {
        adapter.setRetweet(status.retweetedStatus.id)
        adapter.notifyDataSetChanged()
    }

    override fun retweetRemoved(status: Status) {
        adapter.removeRetweet(status.id)
        adapter.notifyDataSetChanged()
    }

    override fun setHeaderIndex(index: Int) {
        adapter.currentIndex = index
    }

    // InteractionListener

    override fun favorite(status: Status) {
        presenter.favorite(status)
    }

    override fun retweet(status: Status) {
        AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.retweet_title))
                .setPositiveButton(context.getString(R.string.retweet),
                        { dialogInterface, i -> presenter.retweet(status) })
                .setNegativeButton(R.string.cancel, null)
                .create().show()
    }

    override fun unfavorite(status: Status) {
        presenter.unfavorite(status)
    }

    override fun unretweet(status: Status) {
        Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
    }

    override fun reply(status: Status, user: User) {
        presenter.reply(status, user)
    }

    override fun openTweet(status: Status, user: User) {
        TweetDetailsActivity.launch(context, status.id, user.screenName)
    }

    override fun showUser(user: User) {
        // TODO
    }

    override fun showImage(imageUrl: String) {
        // TODO
    }

    override fun showImages(imageUrls: List<String>, index: Int) {
        // TODO
    }

    override fun showVideo(videoUrl: String, videoType: String) {
        // TODO
    }
}
