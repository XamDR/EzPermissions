package com.maxdr.ezpermss.ui.permissions.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.maxdr.ezpermss.core.PackageManagerHelper
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.ui.permissions.PermissionHelper
import com.maxdr.ezpermss.util.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class PermissionService : Service() {

	private var isRunning = false
	private val job = SupervisorJob()
	private val scope = CoroutineScope(Dispatchers.IO + job)
	private val timer = Timer()
	private var receiver: ScreenOnOffReceiver = ScreenOnOffReceiver { context, isScreenOn ->
		scope.launch {
			if (isScreenOn) {
				grantDangerousPermissions(context)
			}
			else {
				revokeDangerousPermissions(context)
			}
		}
	}

	override fun onBind(intent: Intent): IBinder? = null

	override fun onCreate() {
		super.onCreate()
		registerScreenStatusReceiver()
	}

	override fun onDestroy() {
		super.onDestroy()
		unregisterScreenStatusReceiver()
		job.cancel()
		timer.cancel()
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
			debug("SERVICE", "Starting service...")
			isRunning = true
			startForeground(NOTIFICATION_ID, NotificationHelper.createNotification(this))
			schedulePeriodicInsertionDangerousPermissionInfo()
		}
	}

	private fun schedulePeriodicInsertionDangerousPermissionInfo() {
		val timerTask = object : TimerTask() {
			override fun run() {
				scope.launch {
					PackageManagerHelper(this@PermissionService).insertDangerousPermissions()
				}
			}
		}
		timer.schedule(timerTask, DELAY, PERIOD) // schedule to run periodically every minute
	}

	private fun stop() {
		debug("SERVICE", "Stopping service...")
		stopForeground(true)
		stopSelf()
		isRunning = false
	}

	private fun registerScreenStatusReceiver() {
		val filter = IntentFilter().apply {
			addAction(Intent.ACTION_SCREEN_OFF)
			addAction(Intent.ACTION_SCREEN_ON)
		}
		registerReceiver(receiver, filter)
	}

	private fun unregisterScreenStatusReceiver() {
		unregisterReceiver(receiver)
	}

	private suspend fun grantDangerousPermissions(context: Context) {
//		val appFullName = "com.google.android.keep"
		val appFullName = "com.android.camera2"
		AppRepository.Instance.getDangerousPermissionInfo(appFullName).collect { permissions ->
			for (permission in permissions) {
				if (permission.modified) {
					PermissionHelper.grantDangerousPermission(context, appFullName, permission.name)
					AppRepository.Instance.updateDangerousPermissionInfo(
						packageName = appFullName,
						permissionName = permission.name,
						granted = true,
						modified = false
					)
				}
			}
		}
	}

	private suspend fun revokeDangerousPermissions(context: Context) {
//		val appFullName = "com.google.android.keep"
		val appFullName = "com.android.camera2"
		AppRepository.Instance.getDangerousPermissionInfo(appFullName).collect { permissions ->
			for (permission in permissions) {
				if (permission.granted) {
					PermissionHelper.revokeDangerousPermission(context, appFullName, permission.name)
					AppRepository.Instance.updateDangerousPermissionInfo(
						packageName = appFullName,
						permissionName = permission.name,
						granted = false,
						modified = true
					)
				}
			}
		}
	}

	companion object {
		const val SERVICE_START_ACTION= "START"
		const val SERVICE_STOP_ACTION= "STOP"
		private const val NOTIFICATION_ID = 1
		private const val DELAY = 0L
		private const val PERIOD = 60000L
	}
}