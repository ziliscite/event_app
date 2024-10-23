package com.example.aplikasi_dicoding_event_first.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasi_dicoding_event_first.EventsGridAdapter
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.data.remote.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.databinding.FragmentHomeBinding
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorFragmentNavigator
import com.example.aplikasi_dicoding_event_first.utils.ui.VisibilityHandler

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingAdapter: EventsGridAdapter
    private lateinit var finishedAdapter: EventsListAdapter

    private lateinit var upcomingVisibilityHandler: VisibilityHandler
    private lateinit var finishedVisibilityHandler: VisibilityHandler

    private val viewModel: HomeViewModel by viewModels<HomeViewModel>{
        HomeViewModelFactory.getInstance()
    }

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

        initializeVisibilityHandler()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeAdapters()
        initializeViewModel()
    }

    private fun initializeViewModel(){
        initializeFinishedEvents()
        initializeUpcomingEvents()

        viewModel.finishedErrorState.error.observe(viewLifecycleOwner) {
            errorFinishedPageNavigator.showError(it.first, it.second)
        }

        viewModel.upcomingErrorState.error.observe(viewLifecycleOwner) {
            errorUpcomingPageNavigator.showError(it.first, it.second)
        }
    }

    private fun initializeFinishedEvents() {
        viewModel.finishedEvents.observe(viewLifecycleOwner) {
            when(it) {
                is EventResult.Success -> {
                    showFinishedLoading(false)
                    updateFinished(it.data)
                }
                is EventResult.Error -> {
                    showFinishedLoading(false)
                }
                is EventResult.Loading -> {
                    showFinishedLoading(true)
                }
            }
        }

        viewModel.getFinishedEvents()
    }

    private fun initializeUpcomingEvents() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) {
            when(it) {
                is EventResult.Success -> {
                    showUpcomingLoading(false)
                    updateUpcoming(it.data)
                }
                is EventResult.Error -> {
                    showUpcomingLoading(false)
                }
                is EventResult.Loading -> {
                    showUpcomingLoading(true)
                }
            }
        }

        viewModel.getUpcomingEvents()
    }

    private fun showUpcomingLoading(isLoading: Boolean) {
        upcomingVisibilityHandler.setLoadingState(isLoading, viewModel.upcomingErrorState.error.value?.first ?: false)
    }

    private fun showFinishedLoading(isLoading: Boolean) {
        finishedVisibilityHandler.setLoadingState(isLoading, viewModel.finishedErrorState.error.value?.first ?: false)
    }

    private fun initializeVisibilityHandler() {
        binding.run {
            upcomingVisibilityHandler = VisibilityHandler(pbUpcomingEvents, errorFragmentUpcomingContainer) { isVisible ->
                rvVisibilityCallback(isVisible, rvUpcomingEvents)
            }

            finishedVisibilityHandler = VisibilityHandler(pbFinishedEvents, errorFragmentFinishedContainer) { isVisible ->
                rvVisibilityCallback(isVisible, rvFinishedEvents)
            }
        }
    }

    private fun rvVisibilityCallback(isVisible: Boolean, recyclerView: RecyclerView) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        recyclerView.visibility = visibility
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
