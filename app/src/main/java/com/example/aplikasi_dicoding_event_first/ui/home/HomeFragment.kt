package com.example.aplikasi_dicoding_event_first.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_dicoding_event_first.EventsGridAdapter
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.data.remote.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.databinding.FragmentHomeBinding
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorFragmentNavigator
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorPageDelegate

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingAdapter: EventsGridAdapter
    private lateinit var finishedAdapter: EventsListAdapter

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var errorUpcomingPageNavigator: ErrorFragmentNavigator
    private lateinit var errorFinishedPageNavigator: ErrorFragmentNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        errorUpcomingPageNavigator = ErrorFragmentNavigator(parentFragmentManager, binding.errorFragmentUpcomingContainer)
        errorFinishedPageNavigator = ErrorFragmentNavigator(parentFragmentManager, binding.errorFragmentFinishedContainer)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapters()
        initializeViewModel()

        viewModel.getFinishedEvents()
        viewModel.getUpcomingEvents()
    }

    private fun initializeViewModel(){
        viewModel.finishedEvents.observe(viewLifecycleOwner) {
            updateFinished(it)
        }

        viewModel.upcomingEvents.observe(viewLifecycleOwner) {
            updateUpcoming(it)
        }

        viewModel.finishedErrorState.error.observe(viewLifecycleOwner) {
            errorFinishedPageNavigator.showError(it.first, it.second)
        }

        viewModel.upcomingErrorState.error.observe(viewLifecycleOwner) {
            errorUpcomingPageNavigator.showError(it.first, it.second)
        }

        viewModel.finishedLoadingState.isLoading.observe(viewLifecycleOwner) {
            showLoading(
                it, binding.pbFinishedEvents, viewModel.finishedErrorState,
                binding.rvFinishedEvents, binding.errorFragmentFinishedContainer
            )
        }

        viewModel.upcomingLoadingState.isLoading.observe(viewLifecycleOwner) {
            showLoading(
                it, binding.pbUpcomingEvents, viewModel.upcomingErrorState,
                binding.rvUpcomingEvents, binding.errorFragmentUpcomingContainer
            )
        }
    }

    private fun showLoading(
        isLoading: Boolean, progressBar: ProgressBar, errorState: ErrorPageDelegate,
        recyclerView: RecyclerView, errorFragmentContainer: FragmentContainerView
    ) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            hideUI(errorState, recyclerView, errorFragmentContainer)
        } else {
            progressBar.visibility = View.GONE
            showUI(errorState, recyclerView, errorFragmentContainer)
        }
    }

    private fun hideUI(
        errorState: ErrorPageDelegate,
        recyclerView: RecyclerView,
        errorFragmentContainer: FragmentContainerView
    ) {
        if (errorState.error.value?.first != true) {
            recyclerView.visibility = View.GONE
        } else {
            errorFragmentContainer.visibility = View.GONE
        }
    }

    private fun showUI(
        errorState: ErrorPageDelegate,
        recyclerView: RecyclerView,
        errorFragmentContainer: FragmentContainerView
    ) {
        if (errorState.error.value?.first != true) {
            recyclerView.visibility = View.VISIBLE
        } else {
            errorFragmentContainer.visibility = View.VISIBLE
        }
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

        binding.apply {
            rvUpcomingEvents.layoutManager = upcomingLayoutManager
            rvUpcomingEvents.adapter = upcomingAdapter
            rvUpcomingEvents.setHasFixedSize(true)

            rvFinishedEvents.layoutManager = finishedLayoutManager
            rvFinishedEvents.adapter = finishedAdapter
            rvFinishedEvents.setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        errorUpcomingPageNavigator.removeErrorFragment()
        errorFinishedPageNavigator.removeErrorFragment()
    }
}