package com.example.aplikasi_dicoding_event_first.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.example.aplikasi_dicoding_event_first.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels<SettingsViewModel>{
        SettingsViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTheme()
    }

    private fun setTheme() {
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            binding.switchDarkMode.isChecked = isDarkModeActive
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
