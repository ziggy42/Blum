package com.andreapivetta.blu.ui.newtweet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadUrl
import com.andreapivetta.blu.data.model.UserFollowed
import kotlinx.android.synthetic.main.user_suggested.view.*

/**
 * Created by andrea on 15/10/16.
 */
class UsersAdapter(val listener: OnUserClickListener) :
        RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(container: View) : RecyclerView.ViewHolder(container) {
        var userProfilePicImageView: ImageView = container.userProfilePicImageView
        var userNameTextView: TextView = container.userNameTextView
        var screenNameTextView: TextView = container.screenNameTextView
    }

    var users: List<UserFollowed> = listOf()
    private var shownUsers: List<UserFollowed> = listOf()

    fun filter(prefix: String) {
        shownUsers = users.toMutableList()
                .filter { x -> x.screenName.toLowerCase().startsWith(prefix.toLowerCase()) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = UserViewHolder(
            LayoutInflater.from(parent?.context).inflate(R.layout.user_suggested, parent, false))

    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        val user = shownUsers[position]
        holder?.userProfilePicImageView?.loadUrl(user.profilePicUrl)
        holder?.userNameTextView?.text = user.name
        holder?.screenNameTextView?.text = user.screenName
        holder?.itemView?.setOnClickListener { listener.onUserClicked(user.screenName) }
    }

    override fun getItemCount() = shownUsers.size
}