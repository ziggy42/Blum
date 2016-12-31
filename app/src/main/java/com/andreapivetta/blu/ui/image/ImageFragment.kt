package com.andreapivetta.blu.ui.image

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.download
import com.andreapivetta.blu.common.utils.loadUrlWithoutPlaceholder
import com.andreapivetta.blu.common.utils.shareText
import uk.co.senab.photoview.PhotoViewAttacher


/**
 * Created by andrea on 28/05/16.
 */
class ImageFragment : Fragment() {

    companion object {
        private val TAG_IMAGE = "image"

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
    private val imageActivity: ImageActivity by lazy { activity as ImageActivity }
    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        imageUrl = arguments.getString(TAG_IMAGE)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_image, container, false)
        val tweetImageView = rootView?.findViewById(R.id.tweetImageView) as ImageView

        tweetImageView.loadUrlWithoutPlaceholder(imageUrl)
        tweetImageView.setOnTouchListener { v, event ->
            if (photoViewAttacher == null) {
                photoViewAttacher = PhotoViewAttacher(tweetImageView)
                (photoViewAttacher as PhotoViewAttacher).setOnPhotoTapListener { view, fl1, fl2 ->
                    imageActivity.showToolbar()
                    imageActivity.hideToolbarDelay()
                }
            }

            true
        }

        imageActivity.hideToolbarDelay()

        return rootView
    }

    override fun onDestroyView() {
        photoViewAttacher = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_media, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> shareText(context, getString(R.string.check_out_photo, imageUrl))
            R.id.action_download ->
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
                    download(context, imageUrl)
                else
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            download(context, imageUrl)
        }
    }
}