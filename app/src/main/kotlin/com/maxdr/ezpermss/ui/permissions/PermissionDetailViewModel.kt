package com.maxdr.ezpermss.ui.permissions

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.core.NormalPermissionInfo
import com.maxdr.ezpermss.util.Empty
import com.maxdr.ezpermss.util.debug
import com.maxdr.ezpermss.util.protectionToString
import com.maxdr.ezpermss.util.toTitleCase
import com.topjohnwu.superuser.Shell

class PermissionDetailViewModel(private val app: Application,
								private val packageFullName: String) : AndroidViewModel(app) {

	init {
		fetchNormalPermissions()
		fetchDangerousPermissions()
	}

	val isEmpty = MutableLiveData(true)

	val normalPerssions: LiveData<List<NormalPermissionInfo>> = fetchNormalPermissions()

	val dangerousPermissions: LiveData<List<DangerousPermissionInfo>> = fetchDangerousPermissions()

	val otherPermissions: LiveData<List<String>> = fetchOtherPermissions()

	fun grantDangerousPermission(permissionName: String) {
		val result = Shell.su("pm grant $packageFullName $permissionName").exec()
		debug("RESULT", result.err)
	}

	fun revokeDangerousPermission(permissionName: String) {
		val result = Shell.su("pm revoke $packageFullName $permissionName").exec()
		debug("RESULT", result.err)
	}

	private fun fetchNormalPermissions(): LiveData<List<NormalPermissionInfo>> {
		val normalPermissions = mutableListOf<NormalPermissionInfo>()
		val pm = app.applicationContext.packageManager
		val permissions: Array<String>? = pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS).requestedPermissions

		if (permissions != null) {
			for (permission in permissions) {
				val protectionLevel = getPermissionProtectionLevel(pm, permission)
				if (protectionLevel == PermissionInfo.PROTECTION_NORMAL) {
					val name = getPermissionLabel(pm, permission)
					val summary = getDangerousPermissionDescription(pm, permission)
					normalPermissions.add(NormalPermissionInfo(
						name = name.toString(),
						summary = summary ?: String.Empty
					))
				}
			}
		}
		normalPermissions.sortBy { it.name }
		return MutableLiveData(normalPermissions)
	}

	private fun fetchDangerousPermissions(): LiveData<List<DangerousPermissionInfo>> {
		val dangerousPermissions = mutableListOf<DangerousPermissionInfo>()
		val pm = app.applicationContext.packageManager
		val pi = pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS)
		val permissions: Array<String>? = pi.requestedPermissions

		if (permissions != null) {
			for ((i, permission) in permissions.withIndex()) {
				val protectionLevel = getPermissionProtectionLevel(pm, permission)
				if ((protectionLevel or PermissionInfo.PROTECTION_DANGEROUS) == protectionLevel) {
					val name = getPermissionLabel(pm, permission)
					val summary = getDangerousPermissionDescription(pm, permission)
					val enabled = (pi.requestedPermissionsFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0

					if (!summary.isNullOrEmpty()) {
						dangerousPermissions.add(DangerousPermissionInfo(
							realName = permission,
							name = name ?: permission,
							summary = summary,
							granted = enabled
						))
					}
				}
			}
		}
		dangerousPermissions.sortBy { it.name }
		return MutableLiveData(dangerousPermissions)
	}

	private fun fetchOtherPermissions(): LiveData<List<String>> {
		val otherPermissions = mutableListOf<String>()
		val pm = app.applicationContext.packageManager
		val permissions: Array<String>? = pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS).requestedPermissions

		if (permissions != null) {
			for (permission in permissions) {
				val protectionLevel = getPermissionProtectionLevel(pm, permission)
				if (protectionLevel != PermissionInfo.PROTECTION_NORMAL &&
					(protectionLevel or PermissionInfo.PROTECTION_DANGEROUS) != protectionLevel) {
					val name = getPermissionLabel(pm, permission)

					if (name != null) {
						val result = "$name - ${protectionToString(protectionLevel)}"
						otherPermissions.add(result)
					}
				}
			}
		}
		otherPermissions.sortBy { it }
		return MutableLiveData(otherPermissions)
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
	private fun getPermissionProtectionLevel(pm: PackageManager, permission: String) =
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