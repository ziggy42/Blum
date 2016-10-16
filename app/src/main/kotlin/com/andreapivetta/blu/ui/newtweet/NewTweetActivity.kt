package com.andreapivetta.blu.ui.newtweet

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.settings.AppSettings
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.common.utils.FileUtils
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.mlsdev.rximagepicker.RxImageConverters
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import kotlinx.android.synthetic.main.activity_newtweet.*
import kotlinx.android.synthetic.main.quoted_tweet.*
import java.io.File

class NewTweetActivity : AppCompatActivity(), NewTweetMvpView, OnUserClickListener {

    private val presenter: NewTweetPresenter by lazy { NewTweetPresenter() }
    private val imageAdapter: DeletableImageAdapter by lazy { DeletableImageAdapter() }
    private val usersAdapter: UsersAdapter by lazy { UsersAdapter(this) }

    private val settings: AppSettings by lazy { AppSettingsFactory.getAppSettings(this) }
    private val storage: AppStorage by lazy { AppStorageFactory.getAppStorage() }

    private var quotedTweet: Tweet? = null

    companion object {
        private val TAG_USER_PREFIX = "userPref"
        private val TAG_REPLY_ID = "replyId"
        private val TAG_QUOTED_TWEET = "quotedStatus"

        fun launch(context: Context) {
            context.startActivity(Intent(context, NewTweetActivity::class.java))
        }

        fun launch(context: Context, userPref: String) {
            val intent = Intent(context, NewTweetActivity::class.java)
            intent.putExtra(TAG_USER_PREFIX, userPref)
            context.startActivity(intent)
        }

        fun launch(context: Context, tweet: Tweet) {
            val intent = Intent(context, NewTweetActivity::class.java)
            intent.putExtra(TAG_QUOTED_TWEET, tweet)
            context.startActivity(intent)
        }

        fun launch(context: Context, userPref: String, replyId: Long) {
            val intent = Intent(context, NewTweetActivity::class.java)
            intent.putExtra(TAG_USER_PREFIX, userPref)
                    .putExtra(TAG_REPLY_ID, replyId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtweet)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        toolbar.setNavigationOnClickListener { finish() }
        presenter.attachView(this)

        newTweetEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.afterTextChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.onTextChanged(s.toString(), start, count)
            }
        })

        if (intent.hasExtra(TAG_QUOTED_TWEET))
            setupQuoted()
        else
            setup()
        setupUsersSuggestions()

        if (intent.hasExtra(TAG_USER_PREFIX)) {
            newTweetEditText.setText("${intent.getStringExtra(TAG_USER_PREFIX)} ")
            newTweetEditText.setSelection(newTweetEditText.text.length)
            presenter.afterTextChanged(newTweetEditText.text.toString())
        }

        if (Intent.ACTION_SEND == intent.action && intent.type != null) {
            if ("text/plain" == intent.type) {
                newTweetEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT))
                newTweetEditText.setSelection(newTweetEditText.text.length)
                presenter.afterTextChanged(newTweetEditText.text.toString())
            } else if (intent.type.startsWith("image/")) {
                val selectedImageUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                imageAdapter.imageFiles.add(File(FileUtils.getPath(this, selectedImageUri)))
                photosRecyclerView.visible()
                imageAdapter.notifyDataSetChanged()
                presenter.afterTextChanged(newTweetEditText.text.toString())
            }
        } else if (Intent.ACTION_SEND_MULTIPLE == intent.action && intent.type != null) {
            if (intent.type.startsWith("image/")) {
                val imageUris = intent.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
                if (imageUris != null) {
                    photosRecyclerView.visible()
                    for (i in imageUris.indices) {
                        if (i >= 4) {
                            showTooManyImagesError()
                            break
                        }
                        imageAdapter.imageFiles.add(File(FileUtils.getPath(this, imageUris[i])))
                    }
                    imageAdapter.notifyDataSetChanged()
                    presenter.afterTextChanged(newTweetEditText.text.toString())
                }
            }
        }
    }

    private fun setupUsersSuggestions() {
        if (settings.isUserDataDownloaded()) {
            usersRecyclerView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            usersRecyclerView.adapter = usersAdapter
            usersAdapter.users = storage.getAllUserFollowed()
        }
    }

    private fun setup() {
        photosRecyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
        photosRecyclerView.adapter = imageAdapter

        photoImageButton.setOnClickListener { presenter.takePicture(imageAdapter.itemCount) }
        imageImageButton.setOnClickListener { presenter.grabImage(imageAdapter.itemCount) }
    }

    private fun setupQuoted() {
        quotedStatusLayout.visible()
        photosRecyclerView.visible(false)
        photoImageButton.visible(false)
        imageImageButton.visible(false)

        quotedTweet = intent.getSerializableExtra(TAG_QUOTED_TWEET) as Tweet
        quotedUserNameTextView.text = quotedTweet?.user?.name
        quotedStatusTextView.text = quotedTweet?.text
        if (quotedTweet != null && quotedTweet!!.hasSingleImage()) {
            photoImageView.visible()
            photoImageView.loadUrl(quotedTweet!!.getImageUrl())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_new_tweet, menu)

        val item = menu?.findItem(R.id.action_chars_left)
        MenuItemCompat.setActionView(item, R.layout.menu_chars_left)
        val view = MenuItemCompat.getActionView(item)

        val charsLeft = presenter.charsLeft()
        val charsLeftTextVIew = view.findViewById(R.id.charsLeftTextView) as TextView
        charsLeftTextVIew.text = charsLeft.toString()
        if (charsLeft < 0)
            charsLeftTextVIew.setTextColor(ContextCompat.getColor(this, R.color.red))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_send)
            if (intent.hasExtra(TAG_REPLY_ID))
                presenter.reply(intent.getLongExtra(TAG_REPLY_ID, -1), imageAdapter.imageFiles)
            else if (intent.hasExtra(TAG_QUOTED_TWEET))
                presenter.sendTweet(quotedTweet!!)
            else
                presenter.sendTweet(imageAdapter.imageFiles)
        return true
    }

    override fun getTweet() = newTweetEditText.text.toString()

    override fun setText(text: String?, selection: Int) {
        newTweetEditText.setText(text, TextView.BufferType.EDITABLE)
        newTweetEditText.setSelection(selection)
    }

    override fun getSelectionStart() = newTweetEditText.selectionStart

    override fun showTooManyCharsError() {
        Toast.makeText(this, R.string.too_many_characters, Toast.LENGTH_SHORT).show()
    }

    override fun showTooManyImagesError() {
        Toast.makeText(this, R.string.too_many_images, Toast.LENGTH_SHORT).show()
    }

    override fun showSendTweetError() {
        Toast.makeText(this, R.string.sending_message_error, Toast.LENGTH_SHORT).show()
    }

    override fun refreshToolbar() {
        invalidateOptionsMenu()
    }

    override fun filterUsers(prefix: String) {
        if (settings.isUserDataDownloaded())
            usersAdapter.filter(prefix)
    }

    override fun showSuggestions() {
        usersRecyclerView.visible()
    }

    override fun hideSuggestions() {
        usersRecyclerView.visible(false)
    }

    override fun close() {
        finish()
    }

    override fun takePicture() {
        RxImagePicker.with(applicationContext).requestImage(Sources.CAMERA)
                .flatMap {
                    RxImageConverters.uriToFile(applicationContext, it, File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "${System.currentTimeMillis()}_image.jpeg"))
                }
                .subscribe {
                    imageAdapter.imageFiles.add(it)
                    imageAdapter.notifyDataSetChanged()
                }
    }

    override fun grabImage() {
        RxImagePicker.with(applicationContext).requestImage(Sources.GALLERY)
                .flatMap {
                    RxImageConverters.uriToFile(applicationContext, it, File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "${System.currentTimeMillis()}_image.jpeg"))
                }
                .subscribe {
                    imageAdapter.imageFiles.add(it)
                    imageAdapter.notifyDataSetChanged()
                }
    }

    override fun onUserClicked(screenName: String) {
        presenter.onUserSelected(screenName)
    }
}
