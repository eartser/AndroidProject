package com.elizavetaartser.androidproject.ui.userlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.entity.User

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var userList: List<User> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.avatarImageView)
            .load(userList[position].avatar)
            .circleCrop()
            .into(holder.avatarImageView)
        holder.userNameTextView.text = userList[position].login
        holder.groupNameTextView.text = userList[position].group_name
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarImageView = itemView.findViewById<ImageView>(R.id.avatarImageView)
        val userNameTextView = itemView.findViewById<TextView>(R.id.userNameTextView)
        val groupNameTextView = itemView.findViewById<TextView>(R.id.groupNameTextView)
    }

}