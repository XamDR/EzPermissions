package com.maxdr.ezpermss.ui.permissions

import android.app.Application
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.util.Empty
import com.maxdr.ezpermss.util.toTitleCase

class PermissionDetailViewModel(private val app: Application,
								private val packageFullName: String) : AndroidViewModel(app) {

	init {
		fetchNormalPermissions()
		fetchDangerousPermissions()
	}

	val normalPerssions: LiveData<List<String>> = fetchNormalPermissions()

	val dangerousPermissions: LiveData<List<DangerousPermissionInfo>> = fetchDangerousPermissions()

	private fun fetchNormalPermissions(): LiveData<List<String>> {
		val normalPermissions = mutableListOf<String>()
		val pm = app.applicationContext.packageManager
		val permissions: Array<String>? = pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS).requestedPermissions

		if (permissions != null) {
			for (permission in permissions) {
				val protectionLevel = gerPermissionProtectionLevel(pm, permission)
				if (protectionLevel == PermissionInfo.PROTECTION_NORMAL) {
					val name = getPermissionLabel(pm, permission)
					normalPermissions.add(name.toString())
				}
			}
		}
		normalPermissions.sortBy { it }
		return MutableLiveData(normalPermissions)
	}

	private fun fetchDangerousPermissions(): LiveData<List<DangerousPermissionInfo>> {
		val dangerousPermissions = mutableListOf<DangerousPermissionInfo>()
		val pm = app.applicationContext.packageManager
		val permissions: Array<String>? = pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS).requestedPermissions

		if (permissions != null) {
			for (permission in permissions) {
				val protectionLevel = gerPermissionProtectionLevel(pm, permission)
				if (protectionLevel == PermissionInfo.PROTECTION_DANGEROUS) {
					val name = getPermissionLabel(pm, permission)
					val summary = getDangerousPermissionDescription(pm, permission)

					dangerousPermissions.add(DangerousPermissionInfo(
						name = name ?: permission,
						summary = summary ?: String.Empty
					))
				}
			}
		}
		dangerousPermissions.sortBy { it.name }
		return MutableLiveData(dangerousPermissions)
	}

	private fun getPermissionLabel(pm: PackageManager, permission: String): String? {
		return try {
			val label = pm.getPermissionInfo(permission, PackageManager.GET_META_DATA).loadLabel(pm).toString()
			return if (label.contains('.')) label else label.toTitleCase()
		}
		catch (e: PackageManager.NameNotFoundException) { null }
	}

	private fun getDangerousPermissionDescription(pm: PackageManager, permission: String): String? {
		return try {
			return pm.getPermissionInfo(permission, PackageManager.GET_META_DATA).loadDescription(pm)?.toString()?.toTitleCase()
		}
		catch (e: PackageManager.NameNotFoundException) { null }
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