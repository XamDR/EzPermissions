package com.maxdr.ezpermss.ui.permissions.dangerous

import android.annotation.SuppressLint
import android.os.IBinder
import com.maxdr.ezpermss.util.debug
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper

@SuppressLint("PrivateApi")
object PermissionHelper {

	fun grantDangerousPermission(packageName: String, permissionName: String) {
		val iPmClass = Class.forName(CLASS_NAME_PACKAGE_MANAGER)
		val iPmStub = Class.forName(CLASS_NAME_PACKAGE_MANAGER_STUB)
		val asInterfaceMethod = iPmStub.getMethod("asInterface", IBinder::class.java)
		val grantRuntimePermissionMethod = iPmClass.getMethod(
			GRANT_RUNTIME_PERMISSION,
			String::class.java, // package name
			String::class.java, // permission name
			Int::class.java // user ID
		)
		val iPmInstance = asInterfaceMethod.invoke(null,
			ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PACKAGE_SERVICE))
		)
		if (!isPermissionSystemFixed(packageName, permissionName) &&
			(packageName != "com.maxdr.ezpermss" && permissionName != "moe.shizuku.manager.permission.API_V23")) {
			grantRuntimePermissionMethod.invoke(iPmInstance, packageName, permissionName, USER)
			debug(DEBUG_TAG, "Permission $permissionName granted for package $packageName")
		}
	}

	fun revokeDangerousPermission(packageName: String, permissionName: String) {
		val iPmClass = Class.forName(CLASS_NAME_PACKAGE_MANAGER)
		val iPmStub = Class.forName(CLASS_NAME_PACKAGE_MANAGER_STUB)
		val asInterfaceMethod = iPmStub.getMethod("asInterface", IBinder::class.java)
		val revokeRuntimePermissionMethod = iPmClass.getMethod(
			REVOKE_RUNTIME_PERMISSION,
			String::class.java, // package name
			String::class.java, // permission name
			Int::class.java // user ID
		)
		val iPmInstance = asInterfaceMethod.invoke(null,
			ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PACKAGE_SERVICE))
		)
		if (!isPermissionSystemFixed(packageName, permissionName) &&
			(packageName != "com.maxdr.ezpermss" && permissionName != "moe.shizuku.manager.permission.API_V23")) {
			revokeRuntimePermissionMethod.invoke(iPmInstance, packageName, permissionName, USER)
			debug(DEBUG_TAG, "Permission $permissionName revoked for package $packageName")
		}
	}

	private fun isPermissionSystemFixed(packageName: String, permissionName: String): Boolean {
		val iPmClass = Class.forName(CLASS_NAME_PACKAGE_MANAGER)
		val iPmStub = Class.forName(CLASS_NAME_PACKAGE_MANAGER_STUB)
		val asInterfaceMethod = iPmStub.getMethod("asInterface", IBinder::class.java)
		val getPermissionFlagsMethod = iPmClass.getMethod(
			"getPermissionFlags",
			String::class.java, // permission name
			String::class.java, // package name
			Int::class.java // user ID
		)
		val iPmInstance = asInterfaceMethod.invoke(null,
			ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PACKAGE_SERVICE))
		)
		val flags = getPermissionFlagsMethod.invoke(iPmInstance, permissionName, packageName, USER) as Int
		return flags and FLAG_PERMISSION_SYSTEM_FIXED != 0
	}

	private const val CLASS_NAME_PACKAGE_MANAGER = "android.content.pm.IPackageManager"
	private const val CLASS_NAME_PACKAGE_MANAGER_STUB = "android.content.pm.IPackageManager\$Stub"
	private const val PACKAGE_SERVICE = "package"
	private const val GRANT_RUNTIME_PERMISSION = "grantRuntimePermission"
	private const val REVOKE_RUNTIME_PERMISSION = "revokeRuntimePermission"
	private const val USER = 0
	private const val DEBUG_TAG = "RESULT"
	private const val FLAG_PERMISSION_SYSTEM_FIXED = 1 shl 4
}