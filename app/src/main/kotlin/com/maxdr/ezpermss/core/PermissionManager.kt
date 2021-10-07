package com.maxdr.ezpermss.core

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.util.Empty
import com.maxdr.ezpermss.util.toTitleCase

class PermissionManager(private val context: Context) {

	suspend fun insertAppPermissionsInfo() {
		val pm = context.packageManager
		val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

		for (packageInfo in packages) {
			val packageFullName = packageInfo.packageName
			val icon = pm.getApplicationInfo(packageFullName, 0).icon

			if (icon != 0 && pm.getLaunchIntentForPackage(packageFullName) != null) {
				val packageName = pm.getApplicationLabel(packageInfo).toString()
				val drawableIcon = pm.getApplicationIcon(packageFullName)
				val pi	= pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS)
				val permissions: Array<String>? = pi.requestedPermissions
				val permissionsInfo = mutableListOf<PermissionInfo>()

				if (permissions != null) {
					for ((i, permission) in permissions.withIndex()) {
						val protectionLevel = getPermissionProtectionLevel(pm, permission)
						val name = getPermissionLabel(pm, permission)
						val summary = getPermissionDescription(pm, permission)
						val enabled = (pi.requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0

						val permissionInfo = PermissionInfo(
							name = permission,
							simpleName = name ?: permission,
							summary = summary ?: String.Empty,
							protectionLevel = protectionLevel,
							granted = enabled
						)
						permissionsInfo.add(permissionInfo)
					}
				}
				val appInfo = AppInfo(
					name = packageName,
					fullName = packageFullName,
					icon = icon,
					drawableIcon = drawableIcon.toBitmap()
				)
				AppRepository.Instance.insertAppInfoPermissions(appInfo, permissionsInfo)
			}
		}
	}

	@Suppress("DEPRECATION")
	private fun getPermissionProtectionLevel(pm: PackageManager, permission: String) =
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			try {
				pm.getPermissionInfo(permission, 0).protection
			}
			catch (e: PackageManager.NameNotFoundException) {
				PROTECTION_LEVEL_IF_NAME_NOT_FOUND
			}
		}
		else {
			try {
				pm.getPermissionInfo(permission, 0).protectionLevel
			}
			catch (e: PackageManager.NameNotFoundException) {
				PROTECTION_LEVEL_IF_NAME_NOT_FOUND
			}
		}

	private fun getPermissionLabel(pm: PackageManager, permission: String): String? {
		return try {
			val label = pm.getPermissionInfo(permission, PackageManager.GET_META_DATA).loadLabel(pm).toString()
			return if (label.contains('.')) label else label.toTitleCase()
		}
		catch (e: PackageManager.NameNotFoundException) { null }
	}

	private fun getPermissionDescription(pm: PackageManager, permission: String): String? {
		return try {
			return pm.getPermissionInfo(permission, PackageManager.GET_META_DATA).loadDescription(pm)?.toString()?.toTitleCase()
		}
		catch (e: PackageManager.NameNotFoundException) { null }
	}

	companion object {
		private const val PROTECTION_LEVEL_IF_NAME_NOT_FOUND = -1
	}
}