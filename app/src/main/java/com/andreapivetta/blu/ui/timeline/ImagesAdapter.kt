package com.andreapivetta.blu.ui.timeline

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.andreapivetta.blu.R
import com.bumptech.glide.Glide
import twitter4j.ExtendedMediaEntity
import twitter4j.MediaEntity


class ImagesAdapter(private val mediaEntities: Array<ExtendedMediaEntity>, private val context: Context) : RecyclerView.Adapter<ImagesAdapter.VHItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHItem {
        return VHItem(LayoutInflater.from(parent.context).inflate(R.layout.photo, parent, false))
    }

    override fun onBindViewHolder(holder: VHItem, position: Int) {
        Glide.with(context).load(mediaEntities[position].mediaURL).placeholder(R.drawable.placeholder).into(holder.tweetPhotoImageView)
        holder.tweetPhotoImageView.setOnClickListener { /* TODO  */ }
    }

    override fun getItemCount(): Int {
        return mediaEntities.size
    }

    inner class VHItem(container: View) : RecyclerView.ViewHolder(container) {
        var tweetPhotoImageView: ImageView

        init {
            this.tweetPhotoImageView = container.findViewById(R.id.tweetPhotoImageView) as ImageView
        }
    }
}
