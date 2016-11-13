package com.andreapivetta.blu.ui.image

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by andrea on 28/05/16.
 */
class ImageFragment : Fragment() {

    companion object {
        val TAG_IMAGE = "image"

        fun newInstance(imageUrl: String): ImageFragment {
            val imageFragment = ImageFragment()
            val bundle = Bundle()
            bundle.putString(TAG_IMAGE, imageUrl)
            imageFragment.arguments = bundle
            return imageFragment
        }
    }

    private lateinit var imageUrl: String
    private var photoViewAttacher: PhotoViewAttacher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        imageUrl = arguments.getString(TAG_IMAGE)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_image, container, false)
        val tweetImageView = rootView?.findViewById(R.id.tweetImageView) as ImageView

        Glide.with(context).load(imageUrl)
                .asBitmap().dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(tweetImageView)

        tweetImageView.setOnTouchListener { v, event ->
            if (photoViewAttacher == null) {
                photoViewAttacher = PhotoViewAttacher(tweetImageView)
                (photoViewAttacher as PhotoViewAttacher).setOnPhotoTapListener { view, fl1, fl2 ->
                    (activity as ImageActivity).showToolbar()
                    (activity as ImageActivity).hideToolbarDelay()
                }
            }

            true
        }

        return rootView
    }

    override fun onDestroyView() {
        photoViewAttacher = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_share) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.check_out_photo, imageUrl)).type = "text/plain"
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

}