package com.example.navyuga.arthYuga.mainModule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navyuga.arthYuga.common.models.PropertyModel
import com.example.navyuga.databinding.ItemPropertyCardBinding

class PropertyAdapter(
    private val list: List<PropertyModel>,
    private val onItemClick: (PropertyModel) -> Unit // ⚡ Add this callback
) : RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPropertyCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPropertyCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val context = holder.itemView.context

        holder.binding.tvTitle.text = item.title
        holder.binding.tvRentLabel.text = "Rent: ${item.rentAmount}"
        holder.binding.tvRoiLabel.text = "ROI: ${item.roi}%  |  IRR: ${item.irr}"
        holder.binding.progressBar.progress = item.fundedPercentage
        holder.binding.tvFundedPercent.text = "${item.fundedPercentage}% Funded"

        // 1. Load Tenant Logo
        if (item.tenantLogoUrl.isNotEmpty()) {
            Glide.with(context).load(item.tenantLogoUrl).into(holder.binding.ivTenantLogo)
        }

        // 2. Load Property Image (with Default Fallback)
        if (item.imageUrl.isNotEmpty()) {
            Glide.with(context).load(item.imageUrl).into(holder.binding.ivPropertyImage)
        } else {
            // ⚡ Use a default "Building" URL if none is provided
            // You can replace this with any image URL you like
            val defaultImage =
                "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80"
            Glide.with(context).load(defaultImage).into(holder.binding.ivPropertyImage)
        }
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = list.size
}