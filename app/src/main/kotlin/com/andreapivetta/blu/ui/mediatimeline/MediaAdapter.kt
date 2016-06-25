package com.andreapivetta.blu.ui.mediatimeline

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.mediatimeline.holders.MediaViewHolder
import com.andreapivetta.blu.ui.mediatimeline.model.Media
import com.bumptech.glide.Glide
import java.util.*

/**
 * Created by andrea on 24/06/16.
 */
class MediaAdapter(val listener: MediaListener) : RecyclerView.Adapter<MediaViewHolder>() {

    interface MediaListener {
        fun favorite(media: Media)

        fun retweet(media: Media)

        fun unfavorite(media: Media)

        fun unretweet(media: Media)
    }

    var mDataSet: MutableList<Media> = ArrayList()

    override fun onBindViewHolder(holder: MediaViewHolder?, position: Int) {
        val media = mDataSet[position]

        Glide.with(holder?.container?.context).load(media.mediaUrl)
                .placeholder(R.drawable.placeholder).into(holder?.mediaImageView)

        if (media.retweet)
            holder?.retweetImageButton?.setColorFilter(
                    ContextCompat.getColor(holder.container.context, R.color.greenThemeColorPrimary))

        if (media.favorite)
            holder?.favouriteImageButton?.setColorFilter(
                    ContextCompat.getColor(holder.container.context, R.color.red))

        holder?.favouriteImageButton?.setOnClickListener {
            if (media.favorite)
                listener.unfavorite(media)
            else
                listener.favorite(media)
        }

        holder?.retweetImageButton?.setOnClickListener {
            if (media.retweet)
                listener.unretweet(media)
            else
                listener.retweet(media)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            MediaViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.media, parent, false))

    override fun getItemCount() = mDataSet.size
}