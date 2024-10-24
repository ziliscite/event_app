package com.example.aplikasi_dicoding_event_first.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.aplikasi_dicoding_event_first.databinding.FragmentSettingsBinding
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.aplikasi_dicoding_event_first.R
import com.example.aplikasi_dicoding_event_first.worker.ReminderWorker
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(requireContext(), "Notifications permission granted", Toast.LENGTH_SHORT).show()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(requireContext(), "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            } else {
                // When user refused or kinda don't chose anything and spam the button again and again (which I did),
                // the "allow" or "refuse" pop up does not pop up again.
                // So this time, instead of stuck, app will guide the user to app settings and allow it manually.
                showAppSettingsDialog()
            }
        }
    }

    private val viewModel: SettingsViewModel by viewModels<SettingsViewModel>{
        SettingsViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        workManager = WorkManager.getInstance(requireContext())


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setReminderDescription()

        setNotificationSetting()

        setThemeSetting()
    }

    private fun setNotificationSetting() {
        viewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActivated: Boolean ->
            binding.switchDailyReminder.isChecked = isReminderActivated
        }

        binding.switchDailyReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (isNotificationPermissionGranted()) {
                    scheduleDailyReminder()

                    viewModel.saveReminderSetting(true)
                } else {
                    getNotificationPermission()

                    // If in the occasion that user manually remove the notification permission,
                    // also change the setting in the datastore
                    viewModel.saveReminderSetting(false)
                }
            } else {
                cancelDailyReminder()
                viewModel.saveReminderSetting(false)
            }
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun getNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun showAppSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Require Notification Permission")
            .setMessage("To enable daily reminders, please allow the notification permission in the app settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        // Go to the app settings page
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun scheduleDailyReminder() {
        val workInfoList = WorkManager.getInstance(requireContext()).getWorkInfosForUniqueWork(WORKER_NAME).get()
        // Check if the worker is already enqueued or running -- if so, return without enqueuing again
        for (workInfo in workInfoList) {
            if (workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.RUNNING) {
                // Update description
                val instant = Instant.ofEpochMilli(workInfo.nextScheduleTimeMillis)
                val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
                setReminderDescription(localDateTime, R.string.daily_reminder_description_reminded)
                return
            }
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        periodicWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        // Use unique work to prevent scheduling multiple workers
        workManager.enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )

        setReminderDescription(reminder = R.string.daily_reminder_description_reminded)
    }

    private fun cancelDailyReminder() {
        workManager.cancelUniqueWork(WORKER_NAME)
        setReminderDescription()
    }

    private fun setReminderDescription(now: LocalDateTime = LocalDateTime.now(), reminder: Int = R.string.daily_reminder_description) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = now.format(formatter)

        binding.reminderDescription.text = getString(reminder, formattedTime)
    }

    private fun setThemeSetting() {
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            binding.switchDarkMode.isChecked = isDarkModeActive
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
            updateThemeDescription(isChecked)
        }
    }

    private fun updateThemeDescription(isChecked: Boolean) {
        binding.themeDescription.text = if (isChecked) {
            getString(R.string.dark_mode_description_disabled)
        } else {
            getString(R.string.dark_mode_description)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val WORKER_NAME = "event_reminder"
    }
}
