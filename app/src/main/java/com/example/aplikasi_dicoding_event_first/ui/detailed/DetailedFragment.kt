package com.example.aplikasi_dicoding_event_first.ui.detailed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.aplikasi_dicoding_event_first.data.response.Event
import com.example.aplikasi_dicoding_event_first.databinding.FragmentDetailedBinding

class DetailedFragment : Fragment() {
    private var _binding: FragmentDetailedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting the id passed from both upcoming & finished fragments (soon, home too)
        val args: DetailedFragmentArgs by navArgs()
        initializeViewModel(args.id)

        viewModel.getEvent()

        initializeNavigation()

        viewModel.loadingState.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(false)
    }

    private fun manageActionBar(create: Boolean) {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        // The opposite of create = destroy. So, when fragment is created, we destroy it
        actionBar?.setDisplayShowTitleEnabled(!create)
    }

    private fun initializeNavigation() {
        // Make the Activity's ActionBar disappear -- Ay, not the "Action Bar", but just the title
        manageActionBar(true)

        // So that the action bar, when pressed, it will do the animation
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Go back to backStack when the arrow or the back button is pressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Show the the ActionBar again when we go back
        manageActionBar(false)
        _binding = null
    }

    private fun initializeViewModel(eventId: Int) {
        val viewModelFactory = DetailedViewModelFactory(eventId)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailedViewModel::class.java]

        viewModel.event.observe(viewLifecycleOwner) {
            setLayout(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setLayout(event: Event) {
        Glide.with(requireContext())
            .load(event.imageLogo)
            .into(binding.ivDetailEvent)
    }
}
