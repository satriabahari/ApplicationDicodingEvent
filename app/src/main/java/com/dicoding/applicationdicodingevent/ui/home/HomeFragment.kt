package com.dicoding.applicationdicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.applicationdicodingevent.databinding.FragmentHomeBinding
import com.dicoding.applicationdicodingevent.ui.ColHomeAdapter
import com.dicoding.applicationdicodingevent.ui.DetailEventActivity
import com.dicoding.applicationdicodingevent.ui.RowHomeAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var rowAdapter: RowHomeAdapter
    private lateinit var colAdapter: ColHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        setupRecyclerViews()
        setupObservers()
    }

    private fun setupRecyclerViews() {
        rowAdapter = RowHomeAdapter { selectedEvent ->
            val intent = Intent(requireActivity(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id.toString())
            startActivity(intent)
        }
        binding.rvVerticalHome.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvVerticalHome.adapter = rowAdapter

        colAdapter = ColHomeAdapter { selectedEvent ->
            val intent = Intent(requireActivity(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", selectedEvent.id.toString())
            startActivity(intent)
        }
        binding.rvHorizontalHome.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontalHome.adapter = colAdapter
    }

    private fun setupObservers() {
        homeViewModel.events.observe(viewLifecycleOwner) { events ->
            val limitedEvents = events.take(5)
            rowAdapter.submitList(limitedEvents)
            colAdapter.submitList(limitedEvents)
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
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