package com.andreapivetta.blu.ui.search.users

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.ui.profile.UserActivity
import kotlinx.android.synthetic.main.user.view.*
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

        holder?.profilePicImageView?.loadUrl(user.originalProfileImageURL)
        holder?.userNameTextView?.text = user.name
        holder?.screenNameTextView?.text = "@${user.screenName}"
        holder?.descriptionTextView?.text = user.description
        holder?.container?.setOnClickListener {
            UserActivity.launch(holder.container.context!!, user)
        }
    }

    class UserViewHolder(val container: View) : RecyclerView.ViewHolder(container) {

        val profilePicImageView: ImageView = container.userProfilePicImageView
        val userNameTextView: TextView = container.userNameTextView
        val screenNameTextView: TextView = container.screenNameTextView
        val descriptionTextView: TextView = container.descriptionTextView

    }

}