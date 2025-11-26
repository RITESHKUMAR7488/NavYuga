package com.example.estake.searchModule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.estake.common.models.PropertyModel
import com.example.estake.databinding.ItemSearchResultBinding

class SearchAdapter(
    private var list: List<PropertyModel>,
    private val onItemClick: (PropertyModel) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // Bind Text Data
        holder.binding.tvTitle.text = item.title
        holder.binding.tvLocation.text = item.location
        holder.binding.tvPrice.text = item.rentAmount
        holder.binding.tvStatus.text = item.status

        // Bind Image: Prefer Tenant Logo if available, otherwise Property Image
        val imageUrl = item.tenantLogoUrl.ifEmpty { item.imageUrl }
        if (imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.binding.ivImage)
        }

        // Handle Click
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = list.size

    // Helper to update list efficiently
    fun updateList(newList: List<PropertyModel>) {
        list = newList
        notifyDataSetChanged()
    }
}