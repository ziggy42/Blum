package com.andreapivetta.blu.ui.newtweet

import android.content.Context
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.Common
import java.io.File

/**
 * Created by andrea on 29/05/16.
 */
class DeletableImageAdapter(val context: Context, val imageFiles: MutableList<File>) :
        RecyclerView.Adapter<DeletableImageAdapter.DeletableImageViewHolder>() {

    override fun onBindViewHolder(holder: DeletableImageViewHolder?, position: Int) {
        val thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageFiles[position].absolutePath),
                Common.dpToPx(context, 200), Common.dpToPx(context, 200));
        holder?.photoImageView?.setImageBitmap(thumbImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = DeletableImageViewHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.photo_deletable, parent, false))

    override fun getItemCount() = imageFiles.size

    inner class DeletableImageViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        val photoImageView: ImageView
        val deleteButton: ImageButton

        init {
            photoImageView = root.findViewById(R.id.tweetPhotoImageView) as ImageView
            deleteButton = root.findViewById(R.id.deleteButton) as ImageButton
            deleteButton.setOnClickListener {
                imageFiles.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

    }
}