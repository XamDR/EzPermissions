package com.maxdr.ezpermss.ui.helpers

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.WorkManager
import com.maxdr.ezpermss.R
import java.util.UUID

object NotificationHelper {

	private const val CHANNEL_ID = "SERVICE_CHANNEL"

	@SuppressLint("UnspecifiedImmutableFlag")
	fun createNotification(context: Context, id: UUID): Notification {
		// This PendingIntent can be used to cancel the Worker.
		val intent = WorkManager.getInstance(context).createCancelPendingIntent(id)

		val builder = NotificationCompat.Builder(context, CHANNEL_ID)
			.setContentTitle(context.getString(R.string.foreground_service_notification_title))
			.setTicker(context.getString(R.string.foreground_service_notification_title))
			.setSmallIcon(R.drawable.ic_small_icon)
			.setOngoing(true)
			.addAction(R.drawable.ic_stop_service, context.getString(R.string.stop_service), intent)

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
