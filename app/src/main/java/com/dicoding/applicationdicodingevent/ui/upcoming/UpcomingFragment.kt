package com.dicoding.applicationdicodingevent.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.applicationdicodingevent.databinding.FragmentUpcomingBinding
import com.dicoding.applicationdicodingevent.ui.DetailEventActivity
import com.dicoding.applicationdicodingevent.ui.EventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[UpcomingViewModel::class.java]

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
        binding.rvEvents.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvents.adapter = adapter
    }

    private fun setupObservers() {
        upcomingViewModel.events.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
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
                upcomingViewModel.filterEvents(query)
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