package com.andreapivetta.blu.ui.mediatimeline

import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.Common
import com.andreapivetta.blu.ui.base.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.timeline.TimelineFragment

/**
 * Created by andrea on 24/06/16.
 */
class MediaFragment : Fragment(), MediaMvpView {

    companion object {
        fun newInstance(userId: Long): MediaFragment {
            val fragment = MediaFragment()
            val bundle = Bundle()
            bundle.putLong(TAG_USER_ID, userId)
            fragment.arguments = bundle
            return fragment
        }

        val TAG_LIST = "list"
        val TAG_PAGE = "page"
        val TAG_USER_ID = "page"
    }

    private val presenter: MediaPresenter by lazy { MediaPresenter(arguments.getLong(TAG_USER_ID)) }
    private lateinit var adapter: MediaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)

        adapter = MediaAdapter()
        if (savedInstanceState != null) {
            adapter.mDataSet = savedInstanceState.getStringArrayList(TAG_LIST)
            presenter.page = savedInstanceState.getInt(TimelineFragment.TAG_PAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_timeline, container, false)

        recyclerView = rootView?.findViewById(R.id.tweetsRecyclerView) as RecyclerView
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        loadingProgressBar = rootView?.findViewById(R.id.loadingProgressBar) as ProgressBar

        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Common.dpToPx(activity, 10)))
        recyclerView.adapter = adapter

        swipeRefreshLayout.setColorSchemeColors(getRefreshColor())

        if (adapter.mDataSet.isEmpty())
            presenter.getPhotos()

        return rootView
    }

    @ColorRes private fun getRefreshColor(): Int {
        val typedValue = TypedValue()
        activity.theme.resolveAttribute(R.attr.appColorPrimary, typedValue, true)
        return typedValue.data
    }

    override fun showPhotos(photos: MutableList<String>) {
        throw UnsupportedOperationException()
    }

    override fun showPhoto(photo: String) {
        adapter.mDataSet.add(photo)
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }
}