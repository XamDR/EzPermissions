package com.maxdr.ezpermss.ui.permissions.service

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.maxdr.ezpermss.MainActivity
import com.maxdr.ezpermss.R

object NotificationHelper {

	private const val CHANNEL_ID = "SERVICE_CHANNEL"
	private const val REQUEST_CODE = 0

	fun createNotification(context: Context): Notification {
		val notificationIntent = Intent(context, MainActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		}
		val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

		val builder = NotificationCompat.Builder(context, CHANNEL_ID)
			.setContentTitle(context.getString(R.string.foreground_service_notification_title))
			.setTicker(context.getString(R.string.foreground_service_notification_title))
			.setContentIntent(pendingIntent)
			.setSmallIcon(R.drawable.ic_small_icon)
			.setOngoing(true)
			.setShowWhen(true)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			createNotificationChannel(context).also {
				builder.setChannelId(it.id)
			}
		}
		return builder.build()
	}

	@TargetApi(Build.VERSION_CODES.O)
	private fun createNotificationChannel(context: Context): NotificationChannel {
		val name = context.getString(R.string.channel_name)
		val descriptionText = context.getString(R.string.channel_desc)
		val importance = NotificationManager.IMPORTANCE_DEFAULT
		val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
			description = descriptionText
			lockscreenVisibility = Notification.VISIBILITY_PUBLIC
			setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), AudioAttributes.Builder().build())
			enableVibration(true)
			enableLights(true)
		}
		val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(channel)
		return channel
	}
}
