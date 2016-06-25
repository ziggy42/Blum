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
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.Common
import com.andreapivetta.blu.ui.base.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.mediatimeline.model.Media
import com.andreapivetta.blu.ui.timeline.TimelineFragment

/**
 * Created by andrea on 24/06/16.
 */
class MediaFragment : Fragment(), MediaMvpView, MediaAdapter.MediaListener {

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

        adapter = MediaAdapter(this)
        if (savedInstanceState != null) {
            adapter.mDataSet = savedInstanceState.getSerializable(TAG_LIST) as MutableList<Media>
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

    override fun showPhoto(media: Media) {
        adapter.mDataSet.add(media)
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

    override fun onNewInteraction() {
        adapter.notifyDataSetChanged()
    }

    // MediaListener
    override fun favorite(media: Media) {
        presenter.favorite(media)
    }

    override fun retweet(media: Media) {
        presenter.retweet(media)
    }

    override fun unfavorite(media: Media) {
        presenter.unfavorite(media)
    }

    override fun unretweet(media: Media) {
        // TODO
        Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
    }
}