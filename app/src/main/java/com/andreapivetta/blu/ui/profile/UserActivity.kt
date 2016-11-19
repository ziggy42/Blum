package com.andreapivetta.blu.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.custom.decorators.SpaceItemDecoration
import com.andreapivetta.blu.ui.image.ImageActivity
import com.andreapivetta.blu.ui.newtweet.NewTweetActivity
import com.andreapivetta.blu.ui.tweetdetails.TweetDetailsActivity
import com.andreapivetta.blu.ui.video.VideoActivity
import kotlinx.android.synthetic.main.activity_user.*
import twitter4j.User

class UserActivity : AppCompatActivity(), UserMvpView {

    companion object {
        val TAG_USER = "user"
        val TAG_USER_SCREEN_NAME = "screen_name"

        fun launch(context: Context, user: User) {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(TAG_USER, user)
            context.startActivity(intent)
        }

        fun launch(context: Context, screenName: String) {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(TAG_USER_SCREEN_NAME, screenName)
            context.startActivity(intent)
        }
    }

    private lateinit var adapter: UserAdapter
    private val presenter = UserPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        adapter = UserAdapter(this)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.addItemDecoration(SpaceItemDecoration(Utils.dpToPx(this, 10)))
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener { presenter.onRefresh() }
        swipeRefreshLayout.setColorSchemeColors(Utils.getRefreshColor(this))

        findViewById(R.id.retryButton)?.setOnClickListener {
            errorView.visible(false)
            presenter.loadUser(intent.getStringExtra(TAG_USER_SCREEN_NAME))
        }

        presenter.attachView(this)
        if (intent.hasExtra(TAG_USER)) {
            hideLoading()
            val user = intent.getSerializableExtra(TAG_USER) as User
            setupUser(user)
            presenter.loadUserData(user)
        } else
            presenter.loadUser(intent.getStringExtra(TAG_USER_SCREEN_NAME))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun setupUser(user: User) {
        adapter.user = user
        adapter.notifyItemInserted(0)
    }

    override fun showTweet(tweet: Tweet) {
        val removedPosition = adapter.tweets.size - 1
        adapter.tweets.removeAt(removedPosition)
        adapter.notifyItemRemoved(removedPosition + 1)

        adapter.tweets.add(0, tweet)
        adapter.notifyItemInserted(1)
    }

    override fun getLastTweetId(): Long = adapter.tweets[0].id

    override fun stopRefresh() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showEmpty() {
        errorView.visible()
    }

    override fun showError() {
        errorView.visible()
    }

    override fun showLoading() {
        loadingProgressBar.visible()
    }

    override fun hideLoading() {
        loadingProgressBar.visible(false)
    }

    override fun showNewTweet(tweet: Tweet, user: User) {
        NewTweetActivity.launch(this, "@${user.screenName}", tweet.id)
    }

    override fun showTweets(tweets: MutableList<Tweet>) {
        adapter.tweets = tweets
        adapter.notifyDataSetChanged()
    }

    override fun showMoreTweets(tweets: MutableList<Tweet>) {
        adapter.tweets.addAll(tweets)
    }

    override fun showSnackBar(stringResource: Int) {
        Snackbar.make(window.decorView.rootView, getString(stringResource), Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun updateRecyclerViewView() {
        adapter.notifyDataSetChanged()
    }

    override fun follow() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun unfollow() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun favorite(tweet: Tweet) {
        presenter.favorite(tweet)
    }

    override fun retweet(tweet: Tweet) {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.retweet_title))
                .setPositiveButton(getString(R.string.retweet),
                        { dialogInterface, i -> presenter.retweet(tweet) })
                .setNeutralButton(getString(R.string.quote),
                        { dialogInterface, i -> NewTweetActivity.launch(this, tweet) })
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
        showNewTweet(tweet, user)
    }

    override fun openTweet(tweet: Tweet, user: User) {
        TweetDetailsActivity.launch(this, tweet.id, user.screenName)
    }

    override fun showUser(user: User) {
        UserActivity.launch(this, user)
    }

    override fun showImage(imageUrl: String) {
        ImageActivity.launch(this, arrayOf(imageUrl))
    }

    override fun showImages(imageUrls: List<String>, index: Int) {
        ImageActivity.launch(this, imageUrls.toTypedArray(), index)
    }

    override fun showVideo(videoUrl: String, videoType: String) {
        VideoActivity.launch(this, videoUrl, videoType)
    }
}
