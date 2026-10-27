package com.turabazaar.app.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.turabazaar.app.MainActivity
import com.turabazaar.app.R

class TurabazaarMessagingService : FirebaseMessagingService() {

    companion object {
        var pushToken: String? = null
        const val CHANNEL_ID = "tura_push"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        pushToken = token
        // Token wird beim nächsten Seitenaufruf an den Server übergeben (siehe MainActivity)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: message.data["title"] ?: "Turabazaar"
        val body = message.notification?.body ?: message.data["body"] ?: ""
        val url = message.data["url"] // z. B. /dashboard oder /produkt/123
        showNotification(title, body, url)
    }

    private fun showNotification(title: String, body: String, url: String?) {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "Turabazaar", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "Bestellungen, Nachrichten & Angebote"
                })
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            url?.let { data = android.net.Uri.parse("https://turabazaar.com$it") }
        }
        val pi = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val n = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()
        nm.notify(System.currentTimeMillis().toInt(), n)
    }
}
