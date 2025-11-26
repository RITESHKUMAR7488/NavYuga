package com.example.estake.searchModule.uis

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estake.R
import com.example.estake.common.utilities.UiState
import com.example.estake.databinding.FragmentSearchBinding
import com.example.estake.mainModule.adapters.PropertyAdapter
import com.example.estake.mainModule.uis.PropertyDetailActivity
import com.example.estake.searchModule.adapters.SearchAdapter
import com.example.estake.searchModule.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()

    // We reuse the PropertyAdapter for results because it looks good
    private lateinit var resultsAdapter: PropertyAdapter
    // You can use a simple string adapter for Recent or build a custom one
    private lateinit var recentAdapter: SearchAdapter

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
        // 1. Countries
        val countries = listOf("India", "USA", "UK", "UAE")
        val countryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, countries)
        binding.actvCountry.setAdapter(countryAdapter)

        // 2. Cities
        val cities = listOf("Kolkata", "Mumbai", "Bangalore", "Delhi", "Gurugram")
        val cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        binding.actvCity.setAdapter(cityAdapter)

        // 3. Currencies
        val currencies = listOf("INR", "USD", "EUR", "AED")
        val currencyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, currencies)
        binding.actvCurrency.setAdapter(currencyAdapter)
    }

    private fun setupRecyclerViews() {
        // Setup Results List (Hidden initially)
        binding.rvSearchResults.layoutManager = LinearLayoutManager(context)
        resultsAdapter = PropertyAdapter(emptyList()) { selectedProperty ->
            val intent = Intent(requireContext(), PropertyDetailActivity::class.java)
            intent.putExtra("property_data", selectedProperty)
            startActivity(intent)
        }
        binding.rvSearchResults.adapter = resultsAdapter

        // Setup Recent List
        binding.rvRecentSearches.layoutManager = LinearLayoutManager(context)
        // For simplicity, we are using a basic adapter logic here or reusing PropertyAdapter if recent items are properties
        // If recent items are just strings, you'd use a simple String Adapter.
        // Let's assume we hide it when searching.
    }

    private fun setupListeners() {
        // Handle Dropdown Changes to update Summary Text
        binding.actvCountry.setOnItemClickListener { _, _, _, _ -> updateSummary() }
        binding.actvCity.setOnItemClickListener { _, _, _, _ -> updateSummary() }
        binding.actvCurrency.setOnItemClickListener { _, _, _, _ -> updateSummary() }

        // Find Button
        binding.btnFindProperties.setOnClickListener {
            viewModel.updateSelection(
                binding.actvCountry.text.toString(),
                binding.actvCity.text.toString(),
                binding.actvCurrency.text.toString()
            )
            viewModel.performSearch()

            // UI Logic: Hide filters/recent, show results
            binding.tvRecentHeader.visibility = View.GONE
            binding.rvRecentSearches.visibility = View.GONE
            binding.rvSearchResults.visibility = View.VISIBLE
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
                if (state is UiState.Success) {
                    // Update the property list with results
                    // We need to cast or create a new PropertyAdapter with the new list
                    binding.rvSearchResults.adapter = PropertyAdapter(state.data) { selectedProperty ->
                        val intent = Intent(requireContext(), PropertyDetailActivity::class.java)
                        intent.putExtra("property_data", selectedProperty)
                        startActivity(intent)
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