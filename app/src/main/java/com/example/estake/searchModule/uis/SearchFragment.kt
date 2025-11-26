package com.example.estake.searchModule.uis

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.estake.common.utilities.UiState
import com.example.estake.databinding.FragmentSearchBinding
import com.example.estake.searchModule.adapters.SearchAdapter
import com.example.estake.mainModule.uis.PropertyDetailActivity
import com.example.estake.searchModule.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchInput()
        observeResults()
    }

    private fun setupRecyclerView() {
        // ⚡ Reusing SearchAdapter (Make sure you created this from previous steps)
        searchAdapter = SearchAdapter(emptyList()) { selectedProperty ->
            val intent = Intent(requireContext(), PropertyDetailActivity::class.java)
            intent.putExtra("property_data", selectedProperty)
            startActivity(intent)
        }

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun setupSearchInput() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.onSearchQueryChanged(s.toString().trim())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun observeResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        binding.layoutEmpty.visibility = View.GONE
                        binding.rvSearchResults.visibility = View.VISIBLE
                        searchAdapter.updateList(state.data)
                    }
                    is UiState.Failure -> {
                        binding.rvSearchResults.visibility = View.GONE
                        binding.layoutEmpty.visibility = View.VISIBLE
                    }
                    is UiState.Idle -> {
                        searchAdapter.updateList(emptyList())
                        binding.layoutEmpty.visibility = View.VISIBLE // Show "Start typing" hint
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