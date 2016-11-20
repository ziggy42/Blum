package com.andreapivetta.blu.ui.search.users

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadAvatar
import com.andreapivetta.blu.ui.profile.UserActivity
import kotlinx.android.synthetic.main.user.view.*
import twitter4j.User
import java.util.*

/**
 * Created by andrea on 25/07/16.
 */
class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    var users: MutableList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = UserViewHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.user, parent, false))

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        val user = users[position]

        holder?.profilePicImageView?.loadAvatar(user.originalProfileImageURL)
        holder?.userNameTextView?.text = user.name
        holder?.screenNameTextView?.text = "@${user.screenName}"
        holder?.descriptionTextView?.text = user.description
        holder?.container?.setOnClickListener {
            UserActivity.launch(holder.container.context!!, user)
        }

        if (user.isVerified)
            holder?.userNameTextView
                    ?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified_user, 0)
    }

    class UserViewHolder(val container: View) : RecyclerView.ViewHolder(container) {

        val profilePicImageView: ImageView = container.userProfilePicImageView
        val userNameTextView: TextView = container.userNameTextView
        val screenNameTextView: TextView = container.screenNameTextView
        val descriptionTextView: TextView = container.descriptionTextView

    }

}