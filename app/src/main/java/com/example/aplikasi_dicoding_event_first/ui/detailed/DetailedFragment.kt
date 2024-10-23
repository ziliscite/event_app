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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.aplikasi_dicoding_event_first.R
import com.example.aplikasi_dicoding_event_first.data.remote.response.Event
import com.example.aplikasi_dicoding_event_first.databinding.FragmentDetailedBinding
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult
import com.example.aplikasi_dicoding_event_first.utils.ui.ErrorFragmentNavigator
import com.example.aplikasi_dicoding_event_first.utils.ui.VisibilityHandler

class DetailedFragment : Fragment() {
    private var _binding: FragmentDetailedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailedViewModel by viewModels<DetailedViewModel>{
        DetailedViewModelFactory.getInstance(requireContext())
    }

    private lateinit var visibilityHandler: VisibilityHandler
    private lateinit var errorPageNavigator: ErrorFragmentNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedBinding.inflate(inflater, container, false)

        visibilityHandler = VisibilityHandler(binding.progressBar, binding.errorFragmentContainer) {
            visibilityUI(it)
        }

        errorPageNavigator = ErrorFragmentNavigator(parentFragmentManager, binding.errorFragmentContainer)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: DetailedFragmentArgs by navArgs()

        initializeViewModel()
        initializeEventDetail(args.id)

        initializeNavigation()
    }

    private fun initializeEventDetail(eventId: Int) {
        viewModel.getEventDetailFavorite(eventId).observe(viewLifecycleOwner) {
            showLoading(true)
            if(it != null) {
                setLayout(
                    it.run {
                        Event(
                            summary = summary,
                            mediaCover = mediaCover,
                            registrants = registrants,
                            imageLogo = imageLogo,
                            link = link,
                            description = description,
                            ownerName = ownerName,
                            cityName = cityName,
                            quota = quota,
                            name = name,
                            id = it.id,
                            beginTime = beginTime,
                            endTime = endTime,
                            category = category
                        )
                    }, isFavorite = true
                )
                showLoading(false)
            } else {
                viewModel.detailEvent.observe(viewLifecycleOwner) { event ->
                    when(event) {
                        is EventResult.Success -> {
                            showLoading(false)
                            setLayout(event.data.event, event.data.isFavorite)
                        }
                        is EventResult.Error -> {
                            showLoading(false)
                        }
                        is EventResult.Loading -> {
                            showLoading(true)
                        }
                    }
                }
                viewModel.getEventDetail(eventId)
            }
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(false)
    }

    private fun manageActionBar(create: Boolean) {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayShowTitleEnabled(!create)
    }

    private fun initializeNavigation() {
        manageActionBar(true)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun initializeViewModel() {
        viewModel.errorState.error.observe(viewLifecycleOwner) {
            errorPageNavigator.showError(it.first, it.second)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        visibilityHandler.setLoadingState(isLoading, viewModel.errorState.error.value?.first ?: false)
    }

    private fun setLayout(event: Event, isFavorite: Boolean = false) {
        val quota = event.quota
        val registrants = event.registrants

        binding.apply {
            fabFavorite.setImageResource(
                if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
            )

            btnVisitLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.link)
                startActivity(intent)
            }

            Glide.with(requireContext())
                .load(event.imageLogo)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.baseline_broken_image_24))
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

            fabFavorite.setOnClickListener {
                if (isFavorite) {
                    viewModel.deleteFavorite(event.id)
                } else {
                    viewModel.insertFavorite(event)
                }
            }
        }
    }

    private fun visibilityUI(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE

        binding.apply {
            fabFavorite.visibility = visibility
            btnVisitLink.visibility = visibility
            textView3.visibility = visibility
            textView4.visibility = visibility
            textView5.visibility = visibility
            ivDetailEvent.visibility = visibility
            tvEventTitle.visibility = visibility
            tvOwnerName.visibility = visibility
            tvDescriptions.visibility = visibility
            tvBeginTime.visibility = visibility
            tvEndTime.visibility = visibility
            tvQuota.visibility = visibility
        }
    }

    override fun onPause() {
        super.onPause()
        errorPageNavigator.removeErrorFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        manageActionBar(false)
        _binding = null
    }
}
