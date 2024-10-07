package com.example.aplikasi_dicoding_event_first.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasi_dicoding_event_first.EventsGridAdapter
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.databinding.FragmentHomeBinding
import com.example.aplikasi_dicoding_event_first.ui.finished.FinishedFragmentDirections

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingAdapter: EventsGridAdapter
    private lateinit var finishedAdapter: EventsListAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapters()

        viewModel.upcomingEvents.observe(viewLifecycleOwner) {
            updateUpcoming(it)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) {
            updateFinished(it)
        }

        viewModel.getUpcomingEvents()
        viewModel.getFinishedEvents()
    }

    private fun updateUpcoming(newData: List<ListEventsItem>) {
        upcomingAdapter.submitList(newData)
        binding.rvUpcomingEvents.adapter = upcomingAdapter
    }

    private fun updateFinished(newData: List<ListEventsItem>) {
        finishedAdapter.submitList(newData)
        binding.rvFinishedEvents.adapter = finishedAdapter
    }

    private fun initializeAdapters() {
        val finishedLayoutManager = LinearLayoutManager(requireContext())
        finishedLayoutManager.orientation = LinearLayoutManager.VERTICAL

        val upcomingLayoutManager = LinearLayoutManager(requireContext())
        upcomingLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        upcomingAdapter = EventsGridAdapter {
            val toEventDetail =  HomeFragmentDirections.actionNavigationHomeToDetailedFragment(it)
            findNavController().navigate(toEventDetail)
        }

        finishedAdapter = EventsListAdapter {
            val toEventDetail =  HomeFragmentDirections.actionNavigationHomeToDetailedFragment(it)
            findNavController().navigate(toEventDetail)
        }

        binding.rvUpcomingEvents.layoutManager = upcomingLayoutManager
        binding.rvUpcomingEvents.adapter = upcomingAdapter
        binding.rvUpcomingEvents.setHasFixedSize(true)

        binding.rvFinishedEvents.layoutManager = finishedLayoutManager
        binding.rvFinishedEvents.adapter = finishedAdapter
        binding.rvFinishedEvents.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}