package com.example.aplikasi_dicoding_event_first.utils.ui

import android.view.View

class VisibilityHandler(
    private val progressBar: View,
    private val errorView: View,
    private val contentViewCallback: (Boolean) -> Unit
) {
    fun setLoadingState(
        isLoading: Boolean, hasError: Boolean
    ) {
        if (isLoading) {
            showLoading()
        } else {
            if (hasError) {
                showError()
            } else {
                showContent()
            }
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        contentViewCallback(false)
        errorView.visibility = View.GONE
    }

    private fun showContent() {
        progressBar.visibility = View.GONE
        contentViewCallback(true)
        errorView.visibility = View.GONE
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        contentViewCallback(false)
        errorView.visibility = View.VISIBLE
    }
}