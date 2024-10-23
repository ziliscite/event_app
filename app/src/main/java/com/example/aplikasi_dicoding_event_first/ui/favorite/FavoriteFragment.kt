package com.example.aplikasi_dicoding_event_first.ui.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.aplikasi_dicoding_event_first.EventsFavoriteAdapter
import com.example.aplikasi_dicoding_event_first.data.local.entity.FavoriteEventEntity
import com.example.aplikasi_dicoding_event_first.databinding.FragmentFavoriteBinding
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorFragmentNavigator
import com.example.aplikasi_dicoding_event_first.utils.ui.RecyclerViewDelegate

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerViewDelegate: RecyclerViewDelegate<FavoriteEventEntity, ListAdapter<FavoriteEventEntity, *>>
    private lateinit var errorPageNavigator: ErrorFragmentNavigator

    private val viewModel: FavoriteViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        createRecyclerViewDelegate()
        errorPageNavigator = ErrorFragmentNavigator(parentFragmentManager, binding.errorFragmentContainer)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewDelegate.setup()
        viewModel.getFavoriteEvent().observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                errorPageNavigator.showError(true, "No Favorite Event")
            } else {
                errorPageNavigator.showError(false, "")
            }

            recyclerViewDelegate.update(it)
        }
    }

    private fun createRecyclerViewDelegate() {
        recyclerViewDelegate = RecyclerViewDelegate(
            binding.rvEvents,
            LinearLayoutManager(requireContext()),
            EventsFavoriteAdapter{
                val toEventDetail = FavoriteFragmentDirections.actionFavoriteFragmentToDetailedFragment(it)
                findNavController().navigate(toEventDetail)
            }
        )
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
