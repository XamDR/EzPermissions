package com.maxdr.ezpermss.core

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo.PROTECTION_DANGEROUS
import android.os.Build
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.util.toTitleCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PackageManagerHelper(private val context: Context) {

	suspend fun insertAppsInfo() {
		val pm = context.packageManager
		val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

		for (packageInfo in packages) {
			val packageFullName = packageInfo.packageName
			val icon = pm.getApplicationInfo(packageFullName, 0).icon

			if (icon != 0 && pm.getLaunchIntentForPackage(packageFullName) != null) {
				val packageName = pm.getApplicationLabel(packageInfo).toString()
				val isSystemApp = packageInfo.flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
				val pi = pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS)
				val permissions: Array<String>? = pi.requestedPermissions

				val appInfo = AppInfo(
					name = packageName,
					fullName = packageFullName,
					systemApp = isSystemApp,
					numberOfPermissions = permissions?.size ?: 0,
					drawableIconResId = icon
				)
				AppRepository.Instance.insertAppInfo(appInfo)
			}
		}
	}

	suspend fun insertAppInfo(appFullName: String) {
		val packageManager = context.packageManager
		val icon = packageManager.getApplicationInfo(appFullName, 0).icon

		if (icon != 0 && packageManager.getLaunchIntentForPackage(appFullName) != null) {
			val packageInfo = packageManager.getApplicationInfo(appFullName, PackageManager.GET_META_DATA)
			val isSystemApp = packageInfo.flags and (ApplicationInfo.FLAG_SYSTEM or ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
			val pi = packageManager.getPackageInfo(appFullName, PackageManager.GET_PERMISSIONS)
			val permissions: Array<String>? = pi.requestedPermissions

			val appInfo = AppInfo(
				name = packageInfo.loadLabel(packageManager).toString(),
				fullName = appFullName,
				systemApp = isSystemApp,
				numberOfPermissions = permissions?.size ?: 0,
				drawableIconResId = icon
			)
			AppRepository.Instance.insertAppInfo(appInfo)
		}
	}

	suspend fun insertDangerousPermissions() {
		val pm = context.packageManager
		val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

		for (packageInfo in packages) {
			val packageFullName = packageInfo.packageName
			val icon = pm.getApplicationInfo(packageFullName, 0).icon

			if (icon != 0 && pm.getLaunchIntentForPackage(packageFullName) != null) {
				val pi	= pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS)
				val permissions: Array<String>? = pi.requestedPermissions

				if (permissions != null) {
					for ((i, permission) in permissions.withIndex()) {
						val protectionLevel = getPermissionProtectionLevel(pm, permission)
						if ((protectionLevel or PROTECTION_DANGEROUS) == protectionLevel && protectionLevel != -1) {
							val name = getPermissionLabel(pm, permission)
							val summary = getPermissionDescription(pm, permission)
							val enabled = (pi.requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
							val favorite = AppRepository.Instance.getDangerousPermissionFavoriteInfo(packageFullName, permission)

							val dangerousPermissionInfo = DangerousPermissionInfo(
								name = permission,
								simpleName = name ?: permission,
								summary = summary ?: context.getString(R.string.no_permission_summary),
								protectionLevel = protectionLevel,
								granted = enabled,
								favorite = favorite ?: false,
								appId = packageFullName
							)
							AppRepository.Instance.insertDangerousPermissionInfo(dangerousPermissionInfo)
						}
					}
				}
			}
		}
	}

	fun fetchGrantValues(appFullName: String): List<Boolean> {
		val grantedValues = mutableListOf<Boolean>()
		val pm = context.packageManager
		val pi = pm.getPackageInfo(appFullName, PackageManager.GET_PERMISSIONS)
		val permissions: Array<String>? = pi.requestedPermissions

		if (permissions != null) {
			for ((i, permission) in permissions.withIndex()) {
				val protectionLevel = getPermissionProtectionLevel(pm, permission)
				if ((protectionLevel or PROTECTION_DANGEROUS) == protectionLevel && protectionLevel != -1) {
					val enabled = (pi.requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
					grantedValues.add(enabled)
				}
			}
		}
		return grantedValues
	}

	fun fetchNonDangerousPermissions(appFullName: String): Flow<List<NonDangerousPermissionInfo>> {
		val nonDangerousPermissions = mutableListOf<NonDangerousPermissionInfo>()
		val pm = context.packageManager
		val permissions: Array<String>? = pm.getPackageInfo(appFullName, PackageManager.GET_PERMISSIONS).requestedPermissions

		if (permissions != null) {
			for (permission in permissions) {
				val protectionLevel = getPermissionProtectionLevel(pm, permission)
				if ((protectionLevel or PROTECTION_DANGEROUS) != protectionLevel || protectionLevel == -1) {
					val name = getPermissionLabel(pm, permission)
					val summary = getPermissionDescription(pm, permission)

					nonDangerousPermissions.add(NonDangerousPermissionInfo(
						name = permission,
						simpleName = name ?: permission,
						summary = summary ?: context.getString(R.string.no_permission_summary),
						protectionLevel = protectionLevel
					))
				}
			}
		}
		nonDangerousPermissions.sortBy { it.simpleName }
		return MutableStateFlow(nonDangerousPermissions)
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