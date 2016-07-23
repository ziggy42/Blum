package com.andreapivetta.blu.ui.newtweet

import com.andreapivetta.blu.common.utils.Patterns
import com.andreapivetta.blu.data.TwitterAPI
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.ui.base.BasePresenter
import rx.SingleSubscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Status
import java.io.File

/**
 * Created by andrea on 20/05/16.
 */
class NewTweetPresenter : BasePresenter<NewTweetMvpView>() {

    companion object {
        private val MAX_URL_LENGTH = 23 // it will change
    }

    private var charsLeft: Int = 140
    private var mSubscriber: Subscription? = null

    fun charsLeft() = charsLeft

    fun afterTextChanged(text: String) {
        checkLength(text)
    }

    fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    fun sendTweet(imageFiles: List<File>) {
        when {
            charsLeft < 0 -> mvpView?.showTooManyCharsError()
            imageFiles.size == 0 -> sendTweet(mvpView?.getTweet())
            else -> sendTweet(mvpView?.getTweet(), imageFiles)
        }
    }

    fun sendTweet(quotedTweet: Tweet) {
        when {
            charsLeft < 0 -> mvpView?.showTooManyCharsError()
            else -> sendTweet("${mvpView?.getTweet()} https://twitter.com/" +
                    "${quotedTweet.user.screenName}/status/${quotedTweet.id}")
        }
    }

    fun reply(inReplyToStatusId: Long, imageFiles: List<File>) {
        when {
            charsLeft < 0 -> mvpView?.showTooManyCharsError()
            imageFiles.size == 0 -> sendTweet(mvpView?.getTweet(), inReplyToStatusId)
            else -> sendTweet(mvpView?.getTweet(), imageFiles, inReplyToStatusId)
        }
    }

    fun takePicture(nImages: Int) {
        if (nImages < 4)
            mvpView?.takePicture()
        else
            mvpView?.showTooManyImagesError()
    }

    fun grabImage(nImages: Int) {
        if (nImages < 4)
            mvpView?.grabImage()
        else
            mvpView?.showTooManyImagesError()
    }

    private fun checkLength(text: String) {
        var wordsLength = 0
        var urls = 0

        text.split(" ")
                .forEach { entry -> if (isUrl(entry)) urls++ else wordsLength += entry.length }
        charsLeft = 140 - text.count { c -> c == ' ' } - wordsLength - (urls * MAX_URL_LENGTH)
        mvpView?.refreshToolbar()
    }

    private fun isUrl(text: String) = Patterns.WEB_URL.matcher(text).matches()

    private fun sendTweet(status: String?) {
        mSubscriber = TwitterAPI.updateTwitterStatus(status)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Status>() {
                    override fun onSuccess(value: Status?) {
                        mvpView?.close()
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        mvpView?.showSendTweetError()
                    }
                })
    }

    private fun sendTweet(status: String?, imageFiles: List<File>) {
        mSubscriber = TwitterAPI.updateTwitterStatus(status, imageFiles)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Status>() {
                    override fun onSuccess(value: Status?) {
                        mvpView?.close()
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        mvpView?.showSendTweetError()
                    }
                })
    }

    private fun sendTweet(status: String?, inReplyToStatusId: Long) {
        mSubscriber = TwitterAPI.reply(status, inReplyToStatusId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Status>() {
                    override fun onSuccess(value: Status?) {
                        mvpView?.close()
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        mvpView?.showSendTweetError()
                    }
                })
    }

    private fun sendTweet(status: String?, imageFiles: List<File>, inReplyToStatusId: Long) {
        mSubscriber = TwitterAPI.reply(status, inReplyToStatusId, imageFiles)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<Status>() {
                    override fun onSuccess(value: Status?) {
                        mvpView?.close()
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error?.message)
                        mvpView?.showSendTweetError()
                    }
                })
    }

}