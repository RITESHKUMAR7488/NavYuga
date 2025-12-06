package com.example.navyuga.arthYuga.mainModule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.navyuga.databinding.ItemPortfolioStatBinding

data class PortfolioStat(
    val label: String,
    val value: String,
    val progress: Int,
    val colorHex: String // e.g. "#00E5FF"
)

class PortfolioStatsAdapter(private val stats: List<PortfolioStat>) :
    RecyclerView.Adapter<PortfolioStatsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPortfolioStatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPortfolioStatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = stats[position]
        holder.binding.tvLabel.text = item.label
        holder.binding.tvValue.text = item.value
        holder.binding.progressIndicator.progress = item.progress

        // Parse color string to int
        try {
            val color = android.graphics.Color.parseColor(item.colorHex)
            holder.binding.progressIndicator.setIndicatorColor(color)
        } catch (e: Exception) { }
    }

    override fun getItemCount() = stats.size
}