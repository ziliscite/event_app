package com.example.aplikasi_dicoding_event_first

import android.app.Application
import com.google.android.material.color.DynamicColors

class EventApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
