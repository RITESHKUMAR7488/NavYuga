package com.example.navyuga.arthYuga.mainModule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navyuga.databinding.ItemCarouselImageBinding

class PropertyImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<PropertyImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemCarouselImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemCarouselImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(images[position])
            .into(holder.binding.ivCarouselImage)
    }

    override fun getItemCount() = images.size
}