package com.maxdr.ezpermss.ui.permissions.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.ui.permissions.PermissionHelper
import com.maxdr.ezpermss.util.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ScreenOnOffReceiver(private val action: (context: Context, isScreenOn: Boolean) -> Unit) : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		if (intent.action == Intent.ACTION_SCREEN_ON) {
			debug("RECEIVER", "Screen on")
//			grantDangerousPermissions(context)
			action(context, true)
		}
		else if (intent.action == Intent.ACTION_SCREEN_OFF) {
			debug("RECEIVER", "Screen off")
//			revokeDangerousPermissions(context)
			action(context, false)
		}
	}

	private fun grantDangerousPermissions(context: Context) {
//		CoroutineScope(Dispatchers.Main).launch {
//			AppRepository.Instance.getAppInfo().collect { apps ->
//				for (appInfo in apps) {
//					AppRepository.Instance.getDangerousPermissionInfo(appInfo.fullName).collect { permissions ->
//						for (permission in permissions) {
//							if (permission.modified) {
//								PermissionHelper.grantDangerousPermission(context, appInfo.fullName, permission.name)
//								AppRepository.Instance.updateDangerousPermissionInfo(appInfo.fullName, permission.name, true)
//							}
//						}
//					}
//				}
//			}
//		}
		val appFullName = "com.google.android.keep"
		CoroutineScope(Dispatchers.Main).launch {
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
	}

	private fun revokeDangerousPermissions(context: Context) {
//		CoroutineScope(Dispatchers.Main).launch {
//			AppRepository.Instance.getAppInfo().collect { apps ->
//				for (appInfo in apps) {
//					AppRepository.Instance.getDangerousPermissionInfo(appInfo.fullName).collect { permissions ->
//						for (permission in permissions) {
//							if (permission.granted) {
//								PermissionHelper.revokeDangerousPermission(context, appInfo.fullName, permission.name)
//								AppRepository.Instance.updateDangerousPermissionInfo(appInfo.fullName, permission.name, false)
//							}
//						}
//					}
//				}
//			}
//		}
		val appFullName = "com.google.android.keep"
		CoroutineScope(Dispatchers.Main).launch {
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
	}
}