package com.example.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.data.model.User
import com.example.githubuser.databinding.ItemUserBinding
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class UserAdapter(
    private var mListUser: MutableList<User>,
    private val onItemClickListener: (User, CircleImageView, TextView) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(DIFF_CALLBACK), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        val user = getItem(position)
        val userAvatar = userViewHolder.binding.imgUserAvatar
        val userLogin = userViewHolder.binding.tvUserLogin
        userViewHolder.binding.user = user
        userViewHolder.binding.itemUser.setOnClickListener {
            onItemClickListener(
                user,
                userAvatar,
                userLogin,
            )
        }
    }

    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty()) {
                        mListUser
                    } else {
                        val filterKey = constraint.toString().toLowerCase(Locale.ROOT).trim()
                        mListUser.filter { user ->
                            user.login.toLowerCase(Locale.ROOT).contains(filterKey)
                        }
                    }
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                val publishResults = results?.values as MutableList<User>
                submitList(publishResults)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.avatar_url == newItem.avatar_url &&
                        oldItem.company == newItem.company &&
                        oldItem.followers == newItem.followers &&
                        oldItem.followers_url == newItem.followers_url &&
                        oldItem.following == newItem.following &&
                        oldItem.following_url == newItem.following_url &&
                        oldItem.location == newItem.location &&
                        oldItem.login == newItem.login &&
                        oldItem.name == newItem.name &&
                        oldItem.public_repos == newItem.public_repos
            }
        }
    }
}