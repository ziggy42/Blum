package com.andreapivetta.blu.ui.search.users

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.ui.profile.ProfileActivity
import com.bumptech.glide.Glide
import twitter4j.User
import java.util.*

/**
 * Created by andrea on 25/07/16.
 */
class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    var mDataSet: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = UserViewHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.user, parent, false))

    override fun getItemCount() = mDataSet.size

    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        val user = mDataSet[position]

        Glide.with(holder?.container?.context)
                .load(user.originalProfileImageURL)
                .placeholder(R.drawable.placeholder)
                .into(holder?.profilePicImageView)

        holder?.userNameTextView?.text = user.name
        holder?.screenNameTextView?.text = "@${user.screenName}"
        holder?.descriptionTextView?.text = user.description
        holder?.container?.setOnClickListener {
            ProfileActivity.launch(holder.container.context!!, user)
        }
    }

    inner class UserViewHolder(val container: View) : RecyclerView.ViewHolder(container) {

        val profilePicImageView = container.findViewById(R.id.userProfilePicImageView) as ImageView
        val userNameTextView = container.findViewById(R.id.userNameTextView) as TextView
        val screenNameTextView = container.findViewById(R.id.screenNameTextView) as TextView
        val descriptionTextView = container.findViewById(R.id.descriptionTextView) as TextView

    }

}