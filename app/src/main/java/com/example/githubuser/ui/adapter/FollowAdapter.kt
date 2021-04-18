package com.example.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.data.model.User
import com.example.githubuser.databinding.ItemFollowBinding

class FollowAdapter(private val users: MutableList<User>) :
    RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder =
        FollowViewHolder(
            ItemFollowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(followViewHolder: FollowViewHolder, position: Int) {
        val user = users[position]
        followViewHolder.binding.followUser = user
    }

    override fun getItemCount(): Int {
        return if (users.isNotEmpty()) users.size else 0
    }

    class FollowViewHolder(val binding: ItemFollowBinding) :
        RecyclerView.ViewHolder(binding.root)
}