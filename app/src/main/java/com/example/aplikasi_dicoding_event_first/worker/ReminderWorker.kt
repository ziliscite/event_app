package com.example.aplikasi_dicoding_event_first.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aplikasi_dicoding_event_first.R
import com.example.aplikasi_dicoding_event_first.di.Injection
import com.example.aplikasi_dicoding_event_first.utils.network.EventResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    // Idk if there is something like "worker factory", but here is my attempt, correct me if this is not a good practice
    private val repository = Injection.provideRepository()

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            when (val response = repository.getLatestEvent()) {
                is EventResult.Success -> {
                    val event = response.data
                    sendNotification(event.name, event.summary, event.link)
                    Result.success()
                }
                is EventResult.Error -> {
                    sendNotification("Get Event Failed", response.error, "https://www.dicoding.com/")
                    Result.failure()
                }
                // There is no loading state in the repository.getLatestEvent().
                // I think I could've just leave the Result.failure(), but just in case
                is EventResult.Loading -> {
                    sendNotification("Get Event Failed", " ... ", "https://www.dicoding.com/")
                    Result.failure()
                }
            }
        }
    }

    private fun sendNotification(eventName: String, eventSummary: String, eventLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventLink))

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle(eventName)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentText(eventSummary)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // SDK_INT is always >= 28 (API 30), so, version checking is not required
        // -- recommended by the IDE
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        builder.setChannelId(CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)

        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "reminder_channel"
    }
}
