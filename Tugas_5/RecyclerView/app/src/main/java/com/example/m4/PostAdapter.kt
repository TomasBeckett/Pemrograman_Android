package com.example.m4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter (private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvPostTitle)
        val tvBody: TextView = view.findViewById(R.id.tvPostBody)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = posts[position]
        holder.tvTitle.text = item.title
        holder.tvBody.text = item.body
    }

    override fun getItemCount(): Int {
        return posts.size
    }


}
