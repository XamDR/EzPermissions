package com.maxdr.ezpermss.ui.permissions

import android.app.Application
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PermissionDetailViewModel(private val app: Application,
								private val packageFullName: String) : AndroidViewModel(app) {

	init {
		getNormalPermissions()
	}

	val normalPerssions: LiveData<List<String>> = getNormalPermissions()

	private fun getNormalPermissions(): LiveData<List<String>> {
		val normalPermissions = mutableListOf<String>()
		val pm = app.applicationContext.packageManager
		val permissions: Array<String>? = pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS).requestedPermissions

		if (permissions != null) {
			for (permission in permissions) {
				val protectionLevel = gerPermissionProtectionLevel(pm, permission)
				if (protectionLevel == PermissionInfo.PROTECTION_NORMAL) {
					normalPermissions.add(permission)
				}
			}
		}
		return MutableLiveData(normalPermissions)
	}

	@Suppress("DEPRECATION")
	private fun gerPermissionProtectionLevel(pm: PackageManager, permission: String) =
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			try {
				pm.getPermissionInfo(permission, 0).protection
			}
			catch (e: PackageManager.NameNotFoundException) { PROTECTION_LEVEL_IF_NAME_NOT_FOUND }
		}
		else {
			try {
				pm.getPermissionInfo(permission, 0).protectionLevel
			}
			catch (e: PackageManager.NameNotFoundException) { PROTECTION_LEVEL_IF_NAME_NOT_FOUND }
		}

	companion object {
		private const val PROTECTION_LEVEL_IF_NAME_NOT_FOUND = -1
	}
}