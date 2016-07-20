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
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.Common
import com.andreapivetta.blu.data.twitter.model.Tweet
import com.andreapivetta.blu.ui.base.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.image.ImageActivity
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.timeline.InteractionListener
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

    override fun showTweets(tweets: MutableList<Tweet>) {
        adapter.mDataSet = tweets
        adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(adapter.currentIndex)
    }

    override fun showTweet(tweet: Tweet) {
        throw UnsupportedOperationException()
    }

    override fun showMoreTweets(tweets: MutableList<Tweet>) {
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

    override fun setHeaderIndex(index: Int) {
        adapter.currentIndex = index
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
        Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
    }

    override fun reply(tweet: Tweet, user: User) {
        presenter.reply(tweet, user)
    }

    override fun openTweet(tweet: Tweet, user: User) {
        TweetDetailsActivity.launch(context, tweet.id, user.screenName)
    }

    override fun showUser(user: User) {
        // TODO
    }

    override fun showImage(imageUrl: String) {
        val intent = Intent(context, ImageActivity::class.java)
        intent.putExtra(ImageActivity.TAG_IMAGES, arrayOf(imageUrl))
        startActivity(intent)
    }

    override fun showImages(imageUrls: List<String>, index: Int) {
        val intent = Intent(context, ImageActivity::class.java)
        intent.putExtra(ImageActivity.TAG_IMAGES, imageUrls.toTypedArray())
        startActivity(intent)
    }

    override fun showVideo(videoUrl: String, videoType: String) {
        // TODO
    }

}
