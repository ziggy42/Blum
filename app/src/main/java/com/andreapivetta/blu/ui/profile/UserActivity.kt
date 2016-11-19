package com.andreapivetta.blu.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.custom.decorators.SpaceItemDecoration
import com.andreapivetta.blu.ui.image.ImageActivity
import com.andreapivetta.blu.ui.tweetdetails.TweetDetailsActivity
import com.andreapivetta.blu.ui.video.VideoActivity
import kotlinx.android.synthetic.main.activity_user.*
import twitter4j.User

class UserActivity : AppCompatActivity(), UserMvpView {

    companion object {
        fun launch(context: Context, user: User) {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(TAG_USER, user)
            context.startActivity(intent)
        }

        fun launch(context: Context, screenName: String) {
            // TODO make this method great again
            Toast.makeText(context, "Not implemented! The screenName is $screenName", Toast.LENGTH_SHORT).show()
        }

        val TAG_USER = "user"
        val TAG_USER_SCREEN_NAME = "screen_name"
    }

    private lateinit var adapter: UserAdapter
    private val userPresenter by lazy { UserPresenter(intent.getSerializableExtra(TAG_USER) as User) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        adapter = UserAdapter(this, intent.getSerializableExtra(TAG_USER) as User)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.addItemDecoration(SpaceItemDecoration(Utils.dpToPx(this, 10)))
        userRecyclerView.setHasFixedSize(true)
        userRecyclerView.adapter = adapter

        userPresenter.attachView(this)
        userPresenter.loadUserData()
    }

    override fun onDestroy() {
        super.onDestroy()
        userPresenter.detachView()
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
        throw UnsupportedOperationException("not implemented")
    }

    override fun retweet(tweet: Tweet) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun unfavorite(tweet: Tweet) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun unretweet(tweet: Tweet) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun reply(tweet: Tweet, user: User) {
        throw UnsupportedOperationException("not implemented")
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
