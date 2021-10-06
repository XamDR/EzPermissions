package com.maxdr.ezpermss.ui.permissions.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.maxdr.ezpermss.util.debug

class PermissionService : Service() {

	private var receiver: ScreenOnOffReceiver? = null
	private var isRunning = false

	override fun onBind(intent: Intent): IBinder? = null

	override fun onCreate() {
		super.onCreate()
		registerScreenStatusReceiver()
	}

	override fun onDestroy() {
		super.onDestroy()
		unregisterScreenStatusReceiver()
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		if (intent != null) {
			when (intent.action) {
				SERVICE_START_ACTION -> start()
				SERVICE_STOP_ACTION -> stop()
				else -> debug("ACTION", "No action in the received intent.")
			}
		}
		return START_STICKY
	}

	private fun start() {
		if (!isRunning) {
			debug("SERVICE", "Starting permission service.")
			isRunning = true
			startForeground(NOTIFICATION_ID, NotificationHelper.createNotification(this))
		}
	}

	private fun stop() {
		debug("SERVICE", "Stopping permission service.")
		stopForeground(true)
		stopSelf()
		isRunning = false
	}

	private fun registerScreenStatusReceiver() {
		receiver = ScreenOnOffReceiver()
		val filter = IntentFilter().apply {
			addAction(Intent.ACTION_SCREEN_OFF)
			addAction(Intent.ACTION_SCREEN_ON)
		}
		registerReceiver(receiver, filter)
	}

	private fun unregisterScreenStatusReceiver() {
		if (receiver != null) {
			unregisterReceiver(receiver)
		}
	}

	companion object {
		const val SERVICE_START_ACTION= "START"
		const val SERVICE_STOP_ACTION= "STOP"
		private const val NOTIFICATION_ID = 1
	}
}