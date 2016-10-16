package com.andreapivetta.blu.ui.timeline.holders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.ui.timeline.InteractionListener
import kotlinx.android.synthetic.main.photo_deletable.view.*
import twitter4j.ExtendedMediaEntity


class ImagesAdapter(private val mediaEntities: Array<ExtendedMediaEntity>,
                    private val listener: InteractionListener) :
        RecyclerView.Adapter<ImagesAdapter.VHItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VHItem(LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false))

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        holder.tweetPhotoImageView.loadUrl(mediaEntities[position].mediaURL)
        holder.tweetPhotoImageView.setOnClickListener {
            listener.showImages(mediaEntities.map { mediaEntity -> mediaEntity.mediaURL }, position)
        }
    }

    override fun getItemCount() = mediaEntities.size

    inner class VHItem(container: View) : RecyclerView.ViewHolder(container) {
        var tweetPhotoImageView: ImageView = container.tweetPhotoImageView
    }

}
