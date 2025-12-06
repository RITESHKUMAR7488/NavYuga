package com.example.navyuga.arthYuga.mainModule.uis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navyuga.R
import com.example.navyuga.arthYuga.common.utilities.PreferenceManager
import com.example.navyuga.arthYuga.common.utilities.UiState
import com.example.navyuga.databinding.FragmentProfileBinding
import com.example.navyuga.mainModule.adapters.PortfolioStatsAdapter
import com.example.navyuga.mainModule.adapters.UploadsAdapter
import com.example.navyuga.mainModule.viewModels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    @Inject lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeader()
        setupLists()
        setupDrawerTrigger()
        observeData()
    }

    private fun setupHeader() {
        // 1. Theme Toggle Logic
        val isDark = preferenceManager.getBoolean(PreferenceManager.KEY_IS_DARK_MODE)
        updateThemeIcon(isDark)

        binding.btnThemeToggle.setOnClickListener {
            val newMode = !isDark
            preferenceManager.putBoolean(PreferenceManager.KEY_IS_DARK_MODE, newMode)

            if (newMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            updateThemeIcon(newMode)
        }
    }

    private fun updateThemeIcon(isDark: Boolean) {
        binding.btnThemeToggle.setImageResource(if (isDark) R.drawable.ic_sun else R.drawable.ic_moon)
    }

    private fun setupLists() {
        // Initialize Recycler Views (Data will come later via observer)
        binding.rvPortfolioStats.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.rvUploads.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = UploadsAdapter(5) // Static 5 slots for uploads
        }
    }

    private fun setupDrawerTrigger() {
        binding.btnMenu.setOnClickListener {
            // ⚡ SAFELY open the drawer from MainActivity
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)
            drawer?.openDrawer(GravityCompat.END)
        }
    }

    private fun observeData() {
        // ⚡ The "Brain" of the screen: Listening for data updates
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Watch for Stats Data (List)
                launch {
                    viewModel.statsState.collect { state ->
                        if (state is UiState.Success) {
                            binding.rvPortfolioStats.adapter = PortfolioStatsAdapter(state.data)
                        }
                    }
                }

                // Watch for User Profile Data (Name)
                launch {
                    viewModel.userState.collect { state ->
                        if (state is UiState.Success) {
                            binding.tvUserName.text = state.data["name"]
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}