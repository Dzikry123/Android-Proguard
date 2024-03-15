package com.example.core.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.core.databinding.ListItemBinding
import com.example.core.domain.model.GithubUser

class UserAdapter(private val data: MutableList<GithubUser.Item> = mutableListOf(),
                  private val listener: (GithubUser.Item) -> Unit) :
        RecyclerView.Adapter<UserAdapter.UserViewHolder>() {



    fun setData(data: MutableList<GithubUser.Item>) {
        this.data.clear()
        this.data.addAll(data)
    }

    class UserViewHolder(private val v: ListItemBinding) : RecyclerView.ViewHolder(v.root) {
        fun bind(item: GithubUser.Item, ) {
            v.imageUser.load(item.avatar_url) {
                transformations(CircleCropTransformation())
            }
            v.tvUserSlice.text = item.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       return UserViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            listener(item)
        }
    }
}