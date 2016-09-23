package com.andreapivetta.blu.ui.conversation

import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import rx.SingleSubscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import twitter4j.DirectMessage
import twitter4j.Twitter
import twitter4j.User

/**
 * Created by andrea on 21/09/16.
 */
class ConversationPresenter(val twitter: Twitter, val storage: AppStorage, val otherId: Long) :
        BasePresenter<ConversationMvpView>() {

    private var isLoading = false
    private var loadUserSubscriber: Subscription? = null
    private var sendPrivateMessageSubscriber: Subscription? = null

    override fun attachView(mvpView: ConversationMvpView) {
        super.attachView(mvpView)
        loadUser(otherId)
    }

    override fun detachView() {
        super.detachView()
        loadUserSubscriber?.unsubscribe()
    }

    fun sendPrivateMessage(text: String) {
        checkViewAttached()

        sendPrivateMessageSubscriber = TwitterAPI.sendPrivateMessage(text, otherId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<DirectMessage>() {
                    override fun onSuccess(directMessage: DirectMessage?) {
                        if (directMessage == null) {
                            onError(Exception("DirectMessage can't be null"))
                        } else {
                            val message = PrivateMessage.valueOf(directMessage, otherId, true)
                            storage.savePrivateMessage(message)
                            mvpView?.showNewPrivateMessage(message)
                        }
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error, "Send private message failed")
                        mvpView?.showSendFailed()
                    }

                })
    }

    private fun loadUser(userId: Long) {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true


        loadUserSubscriber = TwitterAPI.showUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<User>() {
                    override fun onSuccess(user: User?) {
                        mvpView?.hideLoading()
                        if (user == null)
                            onError(Exception("User not found"))
                        else {
                            mvpView?.showUserData(user)
                            mvpView?.showConversation(storage.getConversation(userId))
                        }
                    }

                    override fun onError(error: Throwable?) {
                        Timber.e(error, "User lookup failed")
                        mvpView?.hideLoading()
                        mvpView?.showError()
                    }
                })
    }
}