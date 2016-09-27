package com.andreapivetta.blu.ui.mediatimeline

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.Utils
import com.andreapivetta.blu.common.utils.show
import com.andreapivetta.blu.ui.base.custom.decorators.SpaceTopItemDecoration
import com.andreapivetta.blu.ui.mediatimeline.model.Media
import com.andreapivetta.blu.ui.timeline.TimelineFragment
import java.io.Serializable

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
        loadingProgressBar = rootView?.findViewById(R.id.loadingProgressBar) as ProgressBar

        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(SpaceTopItemDecoration(Utils.dpToPx(activity, 10)))
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (linearLayoutManager.childCount + linearLayoutManager.findFirstVisibleItemPosition() + 1 >
                        linearLayoutManager.itemCount - 10)
                    presenter.getMorePhotos()
            }
        })

        if (adapter.mDataSet.isEmpty())
            presenter.getPhotos()

        return rootView
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(TimelineFragment.TAG_TWEET_LIST, adapter.mDataSet as Serializable)
        outState?.putInt(TimelineFragment.TAG_PAGE, presenter.page)
        super.onSaveInstanceState(outState)
    }

    override fun showPhoto(media: Media) {
        adapter.mDataSet.add(media)
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        loadingProgressBar.show()
    }

    override fun hideLoading() {
        loadingProgressBar.show(false)
    }

    override fun onNewInteraction() {
        adapter.notifyDataSetChanged()
    }

    // MediaListener
    override fun favorite(media: Media) {
        presenter.favorite(media)
    }

    override fun retweet(media: Media) {
        AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.retweet_title))
                .setPositiveButton(context.getString(R.string.retweet),
                        { dialogInterface, i -> presenter.retweet(media) })
                .setNegativeButton(R.string.cancel, null)
                .create().show()
    }

    override fun unfavorite(media: Media) {
        presenter.unfavorite(media)
    }

    override fun unretweet(media: Media) {
        // TODO
        Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
    }
}