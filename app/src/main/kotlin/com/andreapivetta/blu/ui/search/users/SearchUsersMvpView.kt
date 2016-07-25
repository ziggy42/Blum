package com.andreapivetta.blu.ui.search.users

import com.andreapivetta.blu.ui.base.MvpView
import twitter4j.User

/**
 * Created by andrea on 25/07/16.
 */
interface SearchUsersMvpView : MvpView {

    fun showUsers(users: MutableList<User>)

    fun showMoreUsers(users: MutableList<User>)

    fun showEmpty()

    fun showError()

    fun showSnackBar(stringResource: Int)

    fun showLoading()

    fun hideLoading()

    fun updateRecyclerViewView()

}