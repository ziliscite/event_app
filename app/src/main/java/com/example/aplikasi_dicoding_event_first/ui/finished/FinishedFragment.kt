package com.example.aplikasi_dicoding_event_first.ui.finished

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.R
import com.example.aplikasi_dicoding_event_first.data.response.ListEventsItem
import com.example.aplikasi_dicoding_event_first.databinding.FragmentFinishedBinding
import com.example.aplikasi_dicoding_event_first.ui.error.ErrorFragment
import com.example.aplikasi_dicoding_event_first.utils.ui.RecyclerViewDelegate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {
    private val viewModel: FinishedViewModel by viewModels()

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerViewDelegate: RecyclerViewDelegate<ListAdapter<ListEventsItem, *>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        createRecyclerViewDelegate()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewDelegate.setup()

        binding.searchButton.setOnClickListener {
            searchEvents(binding.edReview.text.toString())
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // Initialize rv
        initializeEvents()

        viewModel.events.observe(viewLifecycleOwner) {
            it?.let {
                recyclerViewDelegate.update(it)
            }

            // Observe scroll position
            viewModel.scrollState.position.observe(viewLifecycleOwner) { pos ->
                recyclerViewDelegate.setPosition(pos.first, pos.second)
            }
        }

        viewModel.errorState.error.observe(viewLifecycleOwner) {
            showError(it.first, it.second)
        }

        viewModel.loadingState.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun initializeEvents() {
        viewModel.getEvents("")
    }

    private fun createRecyclerViewDelegate() {
        recyclerViewDelegate = RecyclerViewDelegate(
            binding.rvEvents,
            LinearLayoutManager(requireContext()),
            EventsListAdapter{
                val toEventDetail = FinishedFragmentDirections.actionNavigationFinishedToDetailedFragment(it)
                findNavController().navigate(toEventDetail)
            }
        )
    }

    private fun searchEvents(query: String) {
        viewModel.getEvents(query)
    }

    private fun showError(isVisible: Boolean = true, message: String = "") {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.errorFragmentContainer.visibility = visibility

        if (isVisible) {
            // Check if the ErrorFragment is already added
            var errorFragment = parentFragmentManager.findFragmentById(R.id.errorFragmentContainer) as? ErrorFragment
            if (errorFragment == null) {
                // If not, create and add it with the error message
                errorFragment = ErrorFragment.newInstance(message)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.errorFragmentContainer, errorFragment)
                    .commit()
            } else {
                // If it's already added, just update the message
                errorFragment.updateErrorMessage(message)
            }
        }
    }

    private fun removeErrorFragment() {
        val errorFragment = parentFragmentManager.findFragmentById(R.id.errorFragmentContainer) as? ErrorFragment
        if (errorFragment != null) {
            parentFragmentManager.beginTransaction()
                .remove(errorFragment)
                .commit()
        }
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

        binding.edReview.text?.clear()
        removeErrorFragment()
    }
}
