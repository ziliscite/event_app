package com.example.aplikasi_dicoding_event_first.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.aplikasi_dicoding_event_first.databinding.FragmentErrorBinding

class ErrorFragment : Fragment() {
    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!

    // I don't think I'll need viewmodel for this fragment, since no data state is needed to be observed

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)

        // Get the error message from the arguments and set it to the TextView
        val message = arguments?.getString("error_message") ?: "Unknown error"
        binding.tvMessage.text = message

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateErrorMessage(message: String) {
        binding.tvMessage.text = message
    }

    companion object {
        // Create a new instance of ErrorFragment with an error message
        fun newInstance(message: String): ErrorFragment {
            val fragment = ErrorFragment()
            val args = Bundle()
            args.putString("error_message", message)
            fragment.arguments = args
            return fragment
        }
    }
}

