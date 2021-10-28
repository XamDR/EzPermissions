package com.maxdr.ezpermss.ui.permissions.service

import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.maxdr.ezpermss.core.PackageManagerHelper
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.ui.permissions.PermissionHelper
import com.maxdr.ezpermss.util.debug
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*

class PermissionService : LifecycleService() {

	private var isRunning = false
	private var receiver: ScreenOnOffReceiver = ScreenOnOffReceiver { isScreenOn ->
		lifecycleScope.launch {
			if (isScreenOn) {
				grantDangerousPermissions()
			}
			else {
				revokeDangerousPermissions()
			}
		}
	}

	override fun onCreate() {
		super.onCreate()
		registerScreenStatusReceiver()
	}

	override fun onDestroy() {
		super.onDestroy()
		unregisterScreenStatusReceiver()
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		super.onStartCommand(intent, flags, startId)
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
		}
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

	private fun grantDangerousPermissions() {
		lifecycleScope.launch {
			debug("PERMISSION", "Granting permissions")
			val permissions = AppRepository.Instance.getDangerousPermissionInfo("com.google.android.keep")
				.stateIn(lifecycleScope).value
			debug("RESULT", permissions.size)

			for (permission in permissions) {
				if (permission.modified) {
					PermissionHelper.grantDangerousPermission(this@PermissionService, "com.google.android.keep", permission.name)
					AppRepository.Instance.updateDangerousPermissionInfo(
						packageName = "com.google.android.keep",
						permissionName = permission.name,
						granted = true,
						modified = false
					)
				}
			}
		}
	}

	private fun revokeDangerousPermissions() {
		lifecycleScope.launch {
			PackageManagerHelper(this@PermissionService).insertDangerousPermissions()
			debug("TAG", "Insertion done")
			debug("PERMISSION", "Revoking permissions")
			val permissions = AppRepository.Instance.getDangerousPermissionInfo("com.google.android.keep")
				.stateIn(lifecycleScope).value
			debug("RESULT", permissions.size)

			for (permission in permissions) {
				if (permission.granted) {
					PermissionHelper.revokeDangerousPermission(this@PermissionService, "com.google.android.keep", permission.name)
					AppRepository.Instance.updateDangerousPermissionInfo(
						packageName = "com.google.android.keep",
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
	}
}