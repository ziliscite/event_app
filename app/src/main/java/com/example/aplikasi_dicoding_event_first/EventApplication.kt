package com.example.aplikasi_dicoding_event_first

import android.app.Application
import com.google.android.material.color.DynamicColors

class EventApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // un-apply dynamic color that was applied on the first submission
//        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
