package com.meksconway.rickandmorty.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.base.RootActivity
import com.meksconway.rickandmorty.old.MainActivity

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("***TOKEN", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("message0", remoteMessage.data.toString())
        Log.d("message0", remoteMessage.notification.toString())

    }

    private fun sendNotification(title: String?, message: String?, data: String?) {
        try {
            val intent = Intent(this, RootActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            if (!data.isNullOrBlank()) {
                try {
                    intent.data = Uri.parse(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

            val channelId = "CHANNEL_GENERAL"
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
                notificationBuilder.color = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
            } else {
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            }


            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId,
                    "GENERAL",
                    NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}