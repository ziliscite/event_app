package com.example.aplikasi_dicoding_event_first.ui.detailed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.aplikasi_dicoding_event_first.data.response.Event
import com.example.aplikasi_dicoding_event_first.databinding.FragmentDetailedBinding
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorFragmentNavigator

class DetailedFragment : Fragment() {
    private var _binding: FragmentDetailedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailedViewModel
    private lateinit var errorPageNavigator: ErrorFragmentNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedBinding.inflate(inflater, container, false)

        errorPageNavigator = ErrorFragmentNavigator(parentFragmentManager, binding.errorFragmentContainer)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Getting the id passed from both upcoming & finished fragments (soon, home too)
        val args: DetailedFragmentArgs by navArgs()
        initializeViewModel(args.id)

        viewModel.getEvent()

        initializeNavigation()
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

    private fun initializeViewModel(eventId: Int) {
        val viewModelFactory = DetailedViewModelFactory(eventId)
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailedViewModel::class.java]

        viewModel.event.observe(viewLifecycleOwner) {
            setLayout(it)
        }

        viewModel.errorState.error.observe(viewLifecycleOwner) {
            errorPageNavigator.showError(it.first, it.second)
            if (it.first) {
                visibilityUI(false)
            }
        }

        viewModel.loadingState.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
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
            visibilityUI(false)
        } else {
            binding.errorFragmentContainer.visibility = View.GONE
        }
    }

    private fun showUI() {
        if (viewModel.errorState.error.value?.first != true) {
            visibilityUI(true)
        } else {
            binding.errorFragmentContainer.visibility = View.VISIBLE
        }
    }

    private fun visibilityUI(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE

        binding.let {
            it.textView3.visibility = visibility
            it.textView4.visibility = visibility
            it.textView5.visibility = visibility
            it.btnVisitLink.visibility = visibility
            it.ivDetailEvent.visibility = visibility
            it.tvEventTitle.visibility = visibility
            it.tvOwnerName.visibility = visibility
            it.tvDescriptions.visibility = visibility
            it.tvBeginTime.visibility = visibility
            it.tvEndTime.visibility = visibility
            it.tvQuota.visibility = visibility
        }
    }

    private fun setLayout(event: Event) {
        val quota = event.quota
        val registrants = event.registrants
        binding.apply {
            btnVisitLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.link)
                startActivity(intent)
            }

            Glide.with(requireContext())
                .load(event.imageLogo)
                .into(ivDetailEvent)

            tvEventTitle.text = event.name
            tvOwnerName.text = event.ownerName
            tvDescriptions.text = HtmlCompat.fromHtml(
                event.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            tvBeginTime.text = event.beginTime
            tvEndTime.text = event.endTime

            "${quota - registrants}".also {
                binding.tvQuota.text = it
            }
        }
    }

    override fun onPause() {
        super.onPause()
        errorPageNavigator.removeErrorFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Show the the ActionBar again when we go back
        manageActionBar(false)
        _binding = null
    }
}
