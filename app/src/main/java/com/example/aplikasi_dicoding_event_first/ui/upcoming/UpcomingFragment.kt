package com.example.aplikasi_dicoding_event_first.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.aplikasi_dicoding_event_first.EventsGridAdapter
import com.example.aplikasi_dicoding_event_first.data.remote.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.databinding.FragmentUpcomingBinding
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorFragmentNavigator
import com.example.aplikasi_dicoding_event_first.utils.ui.RecyclerViewDelegate

class UpcomingFragment : Fragment() {
    private val viewModel: UpcomingViewModel by viewModels<UpcomingViewModel>{
        UpcomingViewModelFactory.getInstance()
    }

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerViewDelegate: RecyclerViewDelegate<ListEventsItem, ListAdapter<ListEventsItem, *>>
    private lateinit var errorPageNavigator: ErrorFragmentNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)

        createRecyclerViewDelegate()
        errorPageNavigator = ErrorFragmentNavigator(parentFragmentManager, binding.errorFragmentContainer)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewDelegate.setup()

        viewModel.getUpcomingEvents()

        setLatestUpcomingViewModel()
        viewModel.errorState.error.observe(viewLifecycleOwner) {
            errorPageNavigator.showError(it.first, it.second)
        }
    }

    private fun setLatestUpcomingViewModel() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) {
            when(it) {
                is EventResult.Success -> {
                    showLoading(false)
                    recyclerViewDelegate.update(it.data)
                }
                is EventResult.Error -> {
                    showLoading(false)
                }
                is EventResult.Loading -> {
                    showLoading(true)
                }
            }
            viewModel.scrollState.position.observe(viewLifecycleOwner) { pos ->
                recyclerViewDelegate.setPosition(pos.first, pos.second)
            }
        }
    }

    private fun createRecyclerViewDelegate() {
        recyclerViewDelegate = RecyclerViewDelegate(
            binding.rvEvents,
            GridLayoutManager(requireContext(), 2),
            EventsGridAdapter{
                val toEventDetail = UpcomingFragmentDirections.actionNavigationUpcomingToDetailedFragment(it)
                findNavController().navigate(toEventDetail)
            }
        )
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            hideUI()
        } else {
            binding.progressBar.visibility = View.GONE
            showUI()
        }
    }

    private fun hideUI() {
        if (viewModel.errorState.error.value?.first != true) {
            binding.rvEvents.visibility = View.GONE
        } else {
            binding.errorFragmentContainer.visibility = View.GONE
        }
    }

    private fun showUI() {
        if (viewModel.errorState.error.value?.first != true) {
            binding.rvEvents.visibility = View.VISIBLE
        } else {
            binding.errorFragmentContainer.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        val position = recyclerViewDelegate.getPosition()
        viewModel.scrollState.savePosition(position.first, position.second)

        errorPageNavigator.removeErrorFragment()
    }
}
