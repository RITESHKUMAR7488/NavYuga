package com.example.navyuga.arthYuga.mainModule.uis

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navyuga.R
import com.example.navyuga.arthYuga.common.models.PropertyModel
import com.example.navyuga.databinding.FragmentHomeBinding
import com.example.navyuga.mainModule.adapters.PropertyAdapter
import com.example.navyuga.mainModule.adapters.StoryAdapter
import com.example.navyuga.mainModule.adapters.StoryModel
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // ⚡ Lists for different tabs
    private lateinit var availableList: List<PropertyModel>
    private lateinit var fundedList: List<PropertyModel>
    private lateinit var exitedList: List<PropertyModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareDummyData()
        setupStories()
        setupTabs()

        // ⚡ Load "Available" by default
        loadProperties(availableList)
        updateTabUI(binding.tabAvailable)
    }

    private fun prepareDummyData() {
        // 1. Available Properties
        availableList = listOf(
            PropertyModel(
                title = "Reliance Hub, Park Street",
                rentAmount = "₹ 6.5 Lakhs",
                roi = 8.5,
                irr = "12-14%",
                fundedPercentage = 65,
                tenantName = "Reliance",
                tenantLogoUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/4/4c/Reliance_Digital_logo.svg/1200px-Reliance_Digital_logo.svg.png",
                imageUrl = "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80"
            ),
            PropertyModel(
                title = "Tanishq Gold Plaza",
                rentAmount = "₹ 12.0 Lakhs",
                roi = 7.2,
                irr = "10-11%",
                fundedPercentage = 90,
                tenantName = "Tanishq",
                tenantLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/Tanishq_Logo.svg/2560px-Tanishq_Logo.svg.png",
                imageUrl = "https://images.unsplash.com/photo-1582037928769-181f2422677e?auto=format&fit=crop&w=800&q=80"
            )
        )

        // 2. Funded Properties (100% Complete)
        fundedList = listOf(
            PropertyModel(
                title = "Starbucks Cyber Hub",
                rentAmount = "₹ 4.5 Lakhs",
                roi = 6.1,
                irr = "9-11%",
                fundedPercentage = 100,
                status = "Funded",
                tenantName = "Starbucks",
                tenantLogoUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/d/d3/Starbucks_Corporation_Logo_2011.svg/1200px-Starbucks_Corporation_Logo_2011.svg.png",
                imageUrl = "https://images.unsplash.com/photo-1554118811-1e0d58224f24?q=80&w=2047&auto=format&fit=crop"
            ),
            PropertyModel(
                title = "Zudio High Street",
                rentAmount = "₹ 3.8 Lakhs",
                roi = 6.8,
                irr = "10-12%",
                fundedPercentage = 100,
                status = "Funded",
                tenantName = "Zudio",
                tenantLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Zudio_logo.jpg/800px-Zudio_logo.jpg",
                imageUrl = "https://images.unsplash.com/photo-1441986300917-64674bd600d8?q=80&w=2070&auto=format&fit=crop"
            )
        )

        // 3. Exited Properties (Sold for Profit)
        exitedList = listOf(
            PropertyModel(
                title = "Domino's Sector 18",
                rentAmount = "₹ 2.5 Lakhs",
                roi = 14.5, // Higher ROI on Exit
                irr = "18.2%",
                fundedPercentage = 100,
                status = "Exited",
                tenantName = "Dominos",
                tenantLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Domino%27s_pizza_logo.svg/1200px-Domino%27s_pizza_logo.svg.png",
                imageUrl = "https://images.unsplash.com/photo-1552566626-52f8b828add9?q=80&w=2070&auto=format&fit=crop"
            )
        )
    }

    private fun setupStories() {
        val storyList = listOf(
            StoryModel("Reliance", "https://upload.wikimedia.org/wikipedia/en/thumb/4/4c/Reliance_Digital_logo.svg/1200px-Reliance_Digital_logo.svg.png"),
            StoryModel("Tanishq", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/Tanishq_Logo.svg/2560px-Tanishq_Logo.svg.png"),
            StoryModel("Zudio", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/47/Zudio_logo.jpg/800px-Zudio_logo.jpg"),
            StoryModel("HDFC", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/HDFC_Bank_Logo.svg/2560px-HDFC_Bank_Logo.svg.png"),
            StoryModel("Starbucks", "https://upload.wikimedia.org/wikipedia/en/thumb/d/d3/Starbucks_Corporation_Logo_2011.svg/1200px-Starbucks_Corporation_Logo_2011.svg.png")
        )

        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = StoryAdapter(storyList)
        }
    }

    private fun setupTabs() {
        binding.tabAvailable.setOnClickListener {
            updateTabUI(binding.tabAvailable)
            loadProperties(availableList)
        }

        binding.tabFunded.setOnClickListener {
            updateTabUI(binding.tabFunded)
            loadProperties(fundedList)
        }

        binding.tabExited.setOnClickListener {
            updateTabUI(binding.tabExited)
            loadProperties(exitedList)
        }
    }

    private fun updateTabUI(selectedTab: TextView) {
        // 1. Reset all tabs to "Inactive" state
        val defaultTextColor = ContextCompat.getColor(requireContext(), R.color.text_secondary)

        binding.tabAvailable.background = null
        binding.tabAvailable.setTextColor(defaultTextColor)

        binding.tabFunded.background = null
        binding.tabFunded.setTextColor(defaultTextColor)

        binding.tabExited.background = null
        binding.tabExited.setTextColor(defaultTextColor)

        // 2. Set "Active" state for the clicked tab
        selectedTab.setBackgroundResource(R.drawable.bg_gradient_button)
        selectedTab.setTextColor(Color.WHITE)
    }

    private fun loadProperties(list: List<PropertyModel>) {
        binding.rvProperties.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PropertyAdapter(list) { selectedProperty ->
                // This code runs when a card is clicked
                val intent = Intent(requireContext(), PropertyDetailActivity::class.java)
                intent.putExtra("property_data", selectedProperty) // Pass the object
                startActivity(intent)
            }
            // ⚡ Animation to make it look smooth (Optional)
            scheduleLayoutAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}