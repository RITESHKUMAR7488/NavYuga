package com.example.navyuga.arthYuga.mainModule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navyuga.databinding.ItemStoryCircleBinding

// Simple model just for stories
data class StoryModel(val name: String, val imageUrl: String)

class StoryAdapter(private val list: List<StoryModel>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemStoryCircleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemStoryCircleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvStoryName.text = item.name

        // We will use a placeholder if url is empty
        if (item.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.binding.ivStoryLogo)
        }
    }

    override fun getItemCount() = list.size
}