package com.andreapivetta.blu.ui.conversation

import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorage
import com.andreapivetta.blu.data.twitter.TwitterAPI
import com.andreapivetta.blu.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by andrea on 21/09/16.
 */
class ConversationPresenter(private val storage: AppStorage, private val otherId: Long) :
        BasePresenter<ConversationMvpView>() {

    private val disposables = CompositeDisposable()

    private var isLoading = false
    private var messages: MutableList<PrivateMessage>? = null

    override fun attachView(mvpView: ConversationMvpView) {
        super.attachView(mvpView)
        loadUser(otherId)
    }

    override fun detachView() {
        super.detachView()
        disposables.clear()
    }

    fun onNewPrivateMessage() {
        messages = storage.getConversation(otherId)
        storage.setMessagesAsRead(messages!!.toList())
        mvpView?.showConversation(messages!!)
    }

    fun sendPrivateMessage(text: String) {
        checkViewAttached()

        disposables.add(TwitterAPI.sendPrivateMessage(text, otherId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it == null) {
                        Timber.e("Send private message failed")
                        mvpView?.showSendFailed()
                    } else {
                        val message = PrivateMessage.valueOf(it, otherId, true)
                        message.isRead = true
                        storage.savePrivateMessage(message)
                        mvpView?.showNewPrivateMessage()
                    }
                }, {
                    Timber.e(it, "Send private message failed")
                    mvpView?.showSendFailed()
                }))
    }

    private fun loadUser(userId: Long) {
        checkViewAttached()
        mvpView?.showLoading()
        isLoading = true

        disposables.add(TwitterAPI.showUser(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    mvpView?.hideLoading()
                    if (it == null) {
                        Timber.e("User not found")
                        mvpView?.showError()
                    } else {
                        mvpView?.showUserData(it)
                        messages = storage.getConversation(userId)
                        storage.setMessagesAsRead(messages!!.toList())
                        mvpView?.showConversation(messages!!)
                    }
                }, {
                    Timber.e(it, "User lookup failed")
                    mvpView?.hideLoading()
                    mvpView?.showError()
                }))
    }
}