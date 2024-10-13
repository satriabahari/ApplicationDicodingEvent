package com.dicoding.applicationdicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.applicationdicodingevent.databinding.FragmentFinishedBinding
import com.dicoding.applicationdicodingevent.ui.DetailEventActivity
import com.dicoding.applicationdicodingevent.ui.EventAdapter


class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var finishedViewModel: FinishedViewModel
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishedViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FinishedViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupSearch()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter { selectedEvent ->
            val intent = Intent(requireActivity(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id.toString())
            startActivity(intent)
        }
        binding.rvEvents.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvEvents.adapter = adapter
    }

    private fun setupObservers() {
        finishedViewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setupSearch() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text.toString()
                searchBar.setText(query)
                searchView.hide()
                finishedViewModel.filterEvents(query)
                false
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}