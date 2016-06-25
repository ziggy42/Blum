package com.andreapivetta.blu.ui.mediatimeline

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.mediatimeline.holders.MediaViewHolder
import com.bumptech.glide.Glide
import java.util.*

/**
 * Created by andrea on 24/06/16.
 */
class MediaAdapter : RecyclerView.Adapter<MediaViewHolder>() {

    var mDataSet: MutableList<String> = ArrayList()

    override fun onBindViewHolder(holder: MediaViewHolder?, position: Int) {
        Glide.with(holder?.container?.context).load(mDataSet[position]).into(holder?.mediaImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            MediaViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.media, parent, false))

    override fun getItemCount() = mDataSet.size
}