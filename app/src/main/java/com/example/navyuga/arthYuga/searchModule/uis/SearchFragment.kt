package com.example.navyuga.arthYuga.searchModule.uis

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.navyuga.arthYuga.common.utilities.UiState
import com.example.navyuga.databinding.FragmentSearchBinding
import com.example.navyuga.mainModule.adapters.PropertyAdapter
import com.example.navyuga.mainModule.uis.PropertyDetailActivity
import com.example.navyuga.arthYuga.searchModule.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDropdowns()
        setupRecyclerViews()
        setupListeners()
        observeData()
    }

    private fun setupDropdowns() {
        val countries = listOf("India", "USA", "UK", "UAE")
        val countryAdapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, countries)
        binding.actvCountry.setAdapter(countryAdapter)

        val cities = listOf("Kolkata", "Mumbai", "Bangalore", "Delhi", "Gurugram")
        val cityAdapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, cities)
        binding.actvCity.setAdapter(cityAdapter)

        val currencies = listOf("INR", "USD", "EUR", "AED")
        val currencyAdapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, currencies)
        binding.actvCurrency.setAdapter(currencyAdapter)
    }

    private fun setupRecyclerViews() {
        binding.rvSearchResults.layoutManager = LinearLayoutManager(context)
        // Initial Empty Adapter
        binding.rvSearchResults.adapter = PropertyAdapter(emptyList()) { }
    }

    private fun setupListeners() {
        binding.actvCountry.setOnItemClickListener { _, _, _, _ -> updateSummary() }
        binding.actvCity.setOnItemClickListener { _, _, _, _ -> updateSummary() }
        binding.actvCurrency.setOnItemClickListener { _, _, _, _ -> updateSummary() }

        binding.btnFindProperties.setOnClickListener {
            viewModel.updateSelection(
                binding.actvCountry.text.toString(),
                binding.actvCity.text.toString(),
                binding.actvCurrency.text.toString()
            )
            viewModel.performSearch()
        }
    }

    private fun updateSummary() {
        val country = binding.actvCountry.text.toString()
        val city = binding.actvCity.text.toString()
        val currency = binding.actvCurrency.text.toString()
        binding.tvSelectedSummary.text = "Selected: $country, $city, $currency"
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        // Optionally show a progress bar
                        binding.btnFindProperties.text = "Searching..."
                        binding.btnFindProperties.isEnabled = false
                    }
                    is UiState.Success -> {
                        binding.btnFindProperties.text = "Find Properties"
                        binding.btnFindProperties.isEnabled = true

                        // ⚡ Hide Filters, Show Results
                        binding.tvRecentHeader.visibility = View.GONE
                        binding.rvRecentSearches.visibility = View.GONE
                        binding.rvSearchResults.visibility = View.VISIBLE

                        binding.rvSearchResults.adapter = PropertyAdapter(state.data) { selectedProperty ->
                            val intent = Intent(requireContext(), PropertyDetailActivity::class.java)
                            intent.putExtra("property_data", selectedProperty)
                            startActivity(intent)
                        }
                    }
                    is UiState.Failure -> {
                        binding.btnFindProperties.text = "Find Properties"
                        binding.btnFindProperties.isEnabled = true
                        Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}