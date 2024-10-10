package com.example.aplikasi_dicoding_event_first.utils.ui

import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.example.aplikasi_dicoding_event_first.ui.error.ErrorFragment

class ErrorFragmentNavigator(
    private val fragmentManager: FragmentManager,
    private val fragmentContainerView: FragmentContainerView
) {
    fun showError(isVisible: Boolean = true, message: String = "") {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        fragmentContainerView.visibility = visibility

        if (isVisible) {
            // Check if the ErrorFragment is already added
            var errorFragment = fragmentManager.findFragmentById(fragmentContainerView.id) as? ErrorFragment
            if (errorFragment == null) {
                // If not, create and add it with the error message
                errorFragment = ErrorFragment.newInstance(message)
                fragmentManager.beginTransaction()
                    .replace(fragmentContainerView.id, errorFragment)
                    .commit()
            } else {
                // If it's already added, just update the message
                errorFragment.updateErrorMessage(message)
            }
        }
    }

    // Remove fragment if not error / navigate to other fragment.
    // If not removed, it will either somehow crash the app or the error fragment won't show up again
    fun removeErrorFragment() {
        val errorFragment = fragmentManager.findFragmentById(fragmentContainerView.id) as? ErrorFragment
        errorFragment?.let {
            fragmentManager.beginTransaction().remove(it).commit()
        }
    }
}
