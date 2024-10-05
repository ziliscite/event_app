package com.example.aplikasi_dicoding_event_first.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasi_dicoding_event_first.EventsListAdapter
import com.example.aplikasi_dicoding_event_first.databinding.FragmentFinishedBinding
import com.example.aplikasi_dicoding_event_first.utils.ui.EventsListDelegate

class FinishedFragment : Fragment() {
    private val viewModel: FinishedViewModel by viewModels()

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventsListDelegate: EventsListDelegate

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

        eventsListDelegate.setupRecyclerView()

        viewModel.events.observe(viewLifecycleOwner) {
            eventsListDelegate.updateData(it)
        }

        viewModel.getEvents()
    }

    private fun createEventsListDelegate() {
        eventsListDelegate = EventsListDelegate(
            binding.rvEvents,
            LinearLayoutManager(requireContext()),
            EventsListAdapter{
                val toEventDetail = FinishedFragmentDirections.actionNavigationFinishedToDetailedFragment(
                    // Passing the id using safeArgs
                    it
                )

                findNavController().navigate(toEventDetail)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}