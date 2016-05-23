package com.andreapivetta.blu.ui.newtweet

import com.andreapivetta.blu.arch.BasePresenter
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.new.NewTweetMvpView
import rx.SingleSubscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.Status
import java.util.regex.Pattern

/**
 * Created by andrea on 20/05/16.
 */
class NewTweetPresenter : BasePresenter<NewTweetMvpView>() {

    companion object {
        private val MAX_URL_LENGTH = 23; // it will change
    }

    private var charsLeft: Int = 140
    private var mSubscriber: Subscription? = null
    private val urlPattern = Pattern.compile("^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$")

    fun charsLeft(): Int {
        return charsLeft
    }

    fun afterTextChanged(text: String) {
        checkLength(text)
    }

    fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    fun sendTweet() {
        if (charsLeft < 0)
            mvpView?.showTooManyCharsError()
        else
            mSubscriber = TwitterAPI.updateTwitterStatus(mvpView?.getTweet())
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

    fun reply(inReplyToStatusId: Long) {
        if (charsLeft < 0)
            mvpView?.showTooManyCharsError()
        else
            mSubscriber = TwitterAPI.reply(mvpView?.getTweet(), inReplyToStatusId)
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

    private fun checkLength(text: String) {
        var wordsLength = 0
        var urls = 0

        text.split(" ").forEach { entry -> if (isUrl(entry)) urls++ else wordsLength += entry.length }
        charsLeft = 140 - text.count { c -> c == ' ' } - wordsLength - (urls * MAX_URL_LENGTH)
        mvpView?.refreshToolbar()
    }

    private fun isUrl(text: String): Boolean {
        return urlPattern.matcher(text).find()
    }

}