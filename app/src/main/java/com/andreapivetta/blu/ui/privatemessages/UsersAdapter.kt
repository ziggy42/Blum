package com.andreapivetta.blu.ui.privatemessages

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.andreapivetta.blu.R
import com.andreapivetta.blu.common.utils.loadAvatar
import com.andreapivetta.blu.data.model.UserFollowed
import kotlinx.android.synthetic.main.user_followed.view.*

/**
 * Created by andrea on 16/12/16.
 */
class UsersAdapter(var users: List<UserFollowed>, private val rootView: PrivateMessagesMvpView) :
        RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UserViewHolder =
            UserViewHolder(LayoutInflater.from(parent?.context)
                    .inflate(R.layout.user_followed, parent, false))

    override fun onBindViewHolder(holder: UserViewHolder?, position: Int) {
        holder?.setup(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userProfilePicImageView: ImageView = itemView.userProfilePicImageView
        val screenNameTextView: TextView = itemView.screenNameTextView
        val userNameTextView: TextView = itemView.userNameTextView

        fun setup(user: UserFollowed) {
            userProfilePicImageView.loadAvatar(user.profilePicUrl)
            screenNameTextView.text = "@${user.screenName}"
            userNameTextView.text = user.name
            itemView.setOnClickListener { rootView.startConversation(user.id) }
        }

    }

}