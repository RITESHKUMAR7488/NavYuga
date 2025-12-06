package com.example.navyuga.arthYuga.mainModule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.navyuga.databinding.ItemUploadPropertyBinding

class UploadsAdapter(private val count: Int) : RecyclerView.Adapter<UploadsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemUploadPropertyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemUploadPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvPropertyId.text = "Property ${position + 1}"
        holder.binding.tvStatus.text = "#${position + 1}"

        holder.itemView.setOnClickListener {
            // Open Upload Dialog
        }
    }

    override fun getItemCount() = count
}