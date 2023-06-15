package com.jv.parentapp

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.util.*

/**
 * Created by Jignesh Chauhan on 17-02-2023
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG: String = "MyFirebaseMessagingService"
    private val notificationChannel: String = "MyNotifications"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "remoteMessage = " + Gson().toJson(remoteMessage))
        try {
            if (remoteMessage.data.isNotEmpty()) {

                val data = remoteMessage.data

                Log.d(TAG, "onMessageReceived title: " + data["title"])
                Log.d(TAG, "onMessageReceived body : " + data["message"])
                sendNotification(data["title"].toString(), data["message"].toString())
            }
        } catch (t: Throwable) {
            Log.d(TAG, "Error parsing FCM message", t)
        }
    }

    private fun sendNotification(title: String, body: String) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, notificationChannel)
                .setContentTitle(title)
                .setContentText(body).setAutoCancel(true)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.family)

        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            @SuppressLint("WrongConstant") val mChannel = NotificationChannel(
                notificationChannel,
                name, NotificationManager.IMPORTANCE_HIGH
            )
            val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM).build()
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.setSound(defaultSoundUri, attributes)
            notificationManager.createNotificationChannel(mChannel)
            notificationBuilder.setChannelId(notificationChannel) // Channel ID
        } else {
            notificationBuilder.setSound(defaultSoundUri)
        }

        notificationManager.notify(0, notificationBuilder.build())

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }

}