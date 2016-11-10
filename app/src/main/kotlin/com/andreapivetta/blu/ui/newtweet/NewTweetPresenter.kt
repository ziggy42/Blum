package com.andreapivetta.blu.ui.newtweet

import com.andreapivetta.blu.common.utils.Patterns
import com.andreapivetta.blu.data.model.Tweet
import com.andreapivetta.blu.data.twitter.TweetsQueue
import com.andreapivetta.blu.ui.base.BasePresenter
import java.io.File

/**
 * Created by andrea on 20/05/16.
 */
class NewTweetPresenter : BasePresenter<NewTweetMvpView>() {

    private val MAX_URL_LENGTH = 23 // it will change

    private var charsLeft: Int = 140
    private var lastAtIndex: Int = -1

    fun charsLeft() = charsLeft

    fun afterTextChanged(text: String) {
        checkLength(text)
    }

    fun onTextChanged(text: String, start: Int, count: Int) {
        var query: String? = null
        val selectedText = text.substring(start, start + count)
        if (selectedText.length > 1) {
            if (start > 0 && text[(start - 1)] == '@') {
                query = selectedText
                lastAtIndex = start - 1
            } else if (selectedText.startsWith("@")) {
                query = selectedText.substring(1)
                lastAtIndex = start
            }
        } else if (text.isNotEmpty()) {
            val buffer = StringBuilder()
            var i = mvpView!!.getSelectionStart() - 1
            var c = text[i]
            while (c != '@' && c != ' ' && i > 0) {
                buffer.append(c)
                i--
                c = text[i]
            }

            if (c == '@') {
                query = buffer.reverse().toString()
                lastAtIndex = i
            }
        }

        if (query != null) {
            mvpView?.showSuggestions()
            mvpView?.filterUsers(query)
        } else {
            mvpView?.hideSuggestions()
        }
    }

    fun onUserSelected(screenName: String) {
        val text = mvpView?.getTweet()
        val selectionStart = mvpView?.getSelectionStart()
        mvpView?.setText(text?.substring(0, lastAtIndex + 1) + screenName +
                text?.substring(selectionStart!!, text.length), lastAtIndex + screenName.length + 1)
        mvpView?.hideSuggestions()
    }

    fun sendTweet(imageFiles: List<File>) {
        when {
            charsLeft < 0 -> mvpView?.showTooManyCharsError()
            imageFiles.isEmpty() && mvpView?.getTweet().isNullOrEmpty() ->
                mvpView?.showEmptyTweetError()
            imageFiles.isEmpty() -> sendTweet(mvpView?.getTweet())
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
            imageFiles.isEmpty() && mvpView?.getTweet().isNullOrEmpty() ->
                mvpView?.showEmptyTweetError()
            imageFiles.isEmpty() -> sendTweet(mvpView?.getTweet(), inReplyToStatusId)
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

        text.split(" ").forEach { x -> if (isUrl(x)) urls++ else wordsLength += x.length }
        charsLeft = 140 - text.count { x -> x == ' ' } - wordsLength - (urls * MAX_URL_LENGTH)
        mvpView?.refreshToolbar()
    }

    private fun isUrl(text: String) = Patterns.WEB_URL.matcher(text).matches()

    private fun sendTweet(status: String?) {
        if (status != null)
            TweetsQueue.add(TweetsQueue.StatusUpdate.valueOf(status))
        mvpView?.close()
    }

    private fun sendTweet(status: String?, imageFiles: List<File>) {
        if (status != null)
            TweetsQueue.add(TweetsQueue.StatusUpdate.valueOf(status, imageFiles))
        mvpView?.close()
    }

    private fun sendTweet(status: String?, inReplyToStatusId: Long) {
        if (status != null)
            TweetsQueue.add(TweetsQueue.StatusUpdate.valueOf(status, inReplyToStatusId))
        mvpView?.close()
    }

    private fun sendTweet(status: String?, imageFiles: List<File>, inReplyToStatusId: Long) {
        if (status != null)
            TweetsQueue.add(TweetsQueue.StatusUpdate.valueOf(status, imageFiles, inReplyToStatusId))
        mvpView?.close()
    }

}