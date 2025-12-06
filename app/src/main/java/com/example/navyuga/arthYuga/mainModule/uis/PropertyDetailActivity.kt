package com.example.navyuga.arthYuga.mainModule.uis

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.navyuga.arthYuga.common.models.PropertyModel
import com.example.navyuga.databinding.ActivityPropertyDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.navyuga.arthYuga.mainModule.adapters.PropertyImageAdapter
import com.google.android.material.tabs.TabLayoutMediator
import java.net.URLEncoder


@AndroidEntryPoint
class PropertyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyDetailBinding
    private var propertyModel: PropertyModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Get Data from Intent
        // We use the key "property_data" to retrieve the object
        propertyModel = intent.getParcelableExtra("property_data")

        setupUI()
        setupListeners()
        setupCalculator()
    }

    private fun setupUI() {
        propertyModel?.let { item ->
            binding.tvTitle.text = item.title
            binding.tvLocation.text = item.location.ifEmpty { "Premium Location" } // Fallback
            binding.tvMinInvestment.text = "₹${item.minInvestment}"
            binding.tvIrr.text = item.irr
            binding.tvYield.text = "${item.roi}%"
            binding.tvFunded.text = "${item.fundedPercentage}%"
            Glide.with(this).load(item.tenantLogoUrl).into(binding.ivTenantLogo)
            val imageList = mutableListOf<String>()
            if (item.imageUrl.isNotEmpty()) {
                imageList.add(item.imageUrl)
            } else {
                imageList.add("https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80")
            }
            imageList.add("https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=800&q=80") // Office Interior
            imageList.add("https://images.unsplash.com/photo-1497215728101-856f4ea42174?auto=format&fit=crop&w=800&q=80") // Meeting Room
            val adapter = PropertyImageAdapter(imageList)
            binding.viewPagerImages.adapter = adapter
            TabLayoutMediator(binding.tabLayoutIndicator, binding.viewPagerImages) { tab, position ->}.attach()
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener { finish() } // Back Button

        binding.btnInvestNow.setOnClickListener {
            // 1. Get the Amount currently shown on screen
            val amount = binding.tvInvestAmount.text.toString()

            // 2. Get Property Details (Safely handling nulls)
            val name = propertyModel?.title ?: "Unknown Property"
            val loc = propertyModel?.location ?: "Unknown Location"

            // 3. Call the function with the required data
            openWhatsApp(name, loc, amount)
        }
    }

    private fun setupCalculator() {
        // Simple logic: ROI calculation based on slider
        val roi = propertyModel?.roi ?: 8.0
        val minInv = propertyModel?.minInvestment ?: 5000L

        binding.seekBarInvestment.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Formula: Min + (Step * Progress)
                val investAmount = minInv + (progress * 5000)
                val monthlyReturn = (investAmount * (roi / 100)) / 12

                binding.tvInvestAmount.text = "₹ $investAmount"
                binding.tvEstReturn.text = "₹ ${String.format("%.0f", monthlyReturn)} / mo"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
    private fun openWhatsApp(name: String, loc: String, amount: String) {
        try {
            // ⚡ UPDATED NUMBER (Must include country code '91' for India)
            val phoneNumber = "917488253954"

            val message = "Hello eStake Team,\n\nI am interested in investing *$amount* in the property:\n" +
                    "*$name*\n($loc).\n\nPlease guide me with the payment process."

            val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + URLEncoder.encode(message, "UTF-8")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
        }
    }
}