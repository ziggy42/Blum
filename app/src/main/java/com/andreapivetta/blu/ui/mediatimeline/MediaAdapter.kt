package com.andreapivetta.blu.ui.mediatimeline

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.ui.mediatimeline.holders.MediaViewHolder
import com.andreapivetta.blu.ui.mediatimeline.model.Media
import java.util.*

/**
 * Created by andrea on 24/06/16.
 */
class MediaAdapter(val listener: MediaListener) : RecyclerView.Adapter<MediaViewHolder>() {

    var mDataSet: MutableList<Media> = ArrayList()

    override fun onBindViewHolder(holder: MediaViewHolder?, position: Int) {
        val media = mDataSet[position]

        holder?.mediaImageView?.loadUrl(media.mediaUrl)
        holder?.retweetImageButton?.setColorFilter(ContextCompat.getColor(holder.container.context,
                if (media.retweet) R.color.greenThemeColorPrimary else R.color.white))

        holder?.favouriteImageButton?.setColorFilter(ContextCompat.getColor(holder.container.context,
                if (media.favorite) R.color.red else R.color.white))

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