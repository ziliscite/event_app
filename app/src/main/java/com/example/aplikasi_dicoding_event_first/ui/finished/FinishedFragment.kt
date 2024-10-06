package com.example.aplikasi_dicoding_event_first.ui.finished

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.databinding.FragmentFinishedBinding
import com.example.aplikasi_dicoding_event_first.utils.ui.RecyclerViewDelegate

class FinishedFragment : Fragment() {
    private val viewModel: FinishedViewModel by viewModels()

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerViewDelegate: RecyclerViewDelegate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)

        createEventsListDelegate()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewDelegate.setup()

        viewModel.getEvents()

        viewModel.events.observe(viewLifecycleOwner) {
            recyclerViewDelegate.update(it)

            // Observe scroll position
            viewModel.scrollState.position.observe(viewLifecycleOwner) { pos ->
                recyclerViewDelegate.setPosition(pos)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun createEventsListDelegate() {
        recyclerViewDelegate = RecyclerViewDelegate(
            binding.rvEvents,
            LinearLayoutManager(requireContext()),
            EventsListAdapter{
                val toEventDetail = FinishedFragmentDirections.actionNavigationFinishedToDetailedFragment(it)
                findNavController().navigate(toEventDetail)
            }
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.scrollState.savePosition(recyclerViewDelegate.getPosition())
    }
}