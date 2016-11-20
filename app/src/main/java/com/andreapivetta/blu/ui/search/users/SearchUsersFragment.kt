package com.andreapivetta.blu.ui.search.users

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.visible
import com.andreapivetta.blu.ui.custom.decorators.SpaceTopItemDecoration
import twitter4j.User
import java.io.Serializable

/**
 * Created by andrea on 25/07/16.
 */
class SearchUsersFragment : Fragment(), SearchUsersMvpView {

    companion object {
        private val TAG_QUERY = "query"
        private val TAG_USERS_LIST = "users"
        private val TAG_PAGE = "page"

        fun newInstance(query: String): SearchUsersFragment {
            val fragment = SearchUsersFragment()
            val bundle = Bundle()
            bundle.putString(TAG_QUERY, query)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val presenter: SearchUsersPresenter by lazy {
        SearchUsersPresenter(arguments.getString(TAG_QUERY))
    }

    private lateinit var adapter: UsersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var badThingsViewGroup: ViewGroup
    private lateinit var retryButton: Button

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        presenter.attachView(this)

        adapter = UsersAdapter()
        if (bundle != null) {
            adapter.users = bundle.getSerializable(TAG_USERS_LIST) as MutableList<User>
            presenter.page = bundle.getInt(TAG_PAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_timeline, container, false)

        recyclerView = rootView?.findViewById(R.id.tweetsRecyclerView) as RecyclerView
        loadingProgressBar = rootView?.findViewById(R.id.loadingProgressBar) as ProgressBar
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        badThingsViewGroup = rootView?.findViewById(R.id.badThingsViewGroup) as ViewGroup
        retryButton = rootView?.findViewById(R.id.retryButton) as Button

        swipeRefreshLayout.isEnabled = false

        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Utils.dpToPx(activity, 10)))
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (linearLayoutManager.childCount + linearLayoutManager
                        .findFirstVisibleItemPosition() + 1 > linearLayoutManager.itemCount - 10)
                    presenter.getMoreUsers()
            }
        })

        if (adapter.users.isEmpty())
            presenter.getUsers()
        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(TAG_USERS_LIST, adapter.users as Serializable)
        outState?.putInt(TAG_PAGE, presenter.page)
        super.onSaveInstanceState(outState)
    }

    override fun showUsers(users: MutableList<User>) {
        adapter.users = users
        adapter.notifyDataSetChanged()
    }

    override fun showMoreUsers(users: MutableList<User>) {
        adapter.users.addAll(users)
    }

    override fun showEmpty() {
        badThingsViewGroup.visible()
    }

    override fun showError() {
        badThingsViewGroup.visible()
    }

    override fun showSnackBar(stringResource: Int) {
        Snackbar.make(view!!, getString(stringResource), Snackbar.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        loadingProgressBar.visible()
    }

    override fun hideLoading() {
        loadingProgressBar.visible(false)
    }

    override fun updateRecyclerViewView() {
        adapter.notifyDataSetChanged()
    }

}