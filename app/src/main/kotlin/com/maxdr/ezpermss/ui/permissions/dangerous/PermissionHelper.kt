package com.maxdr.ezpermss.ui.permissions.dangerous

import android.annotation.SuppressLint
import android.os.Build
import android.os.IBinder
import com.maxdr.ezpermss.BuildConfig
import com.maxdr.ezpermss.util.debug
import rikka.shizuku.ShizukuBinderWrapper
import rikka.shizuku.SystemServiceHelper
import java.lang.reflect.Method

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
			(packageName != BuildConfig.APPLICATION_ID && permissionName != SHIZUKU_PERMISSION_NAME)) {
			grantRuntimePermissionMethod.invoke(iPmInstance, packageName, permissionName, USER_ID)
			debug(DEBUG_TAG, "Permission $permissionName granted for package $packageName")
		}
	}

	/**
	 * Since Android 11 (R - API level 30) the method **[IPackageManager.revokeRuntimePermission]** has been removed
	 * from IPackageManager.aidl and added to IPermissionManager.aidl.
	 * @see <a href="https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/permission/IPermissionManager.aidl#67">IPermissionManager.revokeRuntimePermission</a>
	 * Besides, this method takes now an extra parameter reason: String, which can be null according to the comments in the source code
	 * @see <a href="https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/permission/PermissionManager.java#401">IPermissionManager.revokeRuntimePermission</a>
	 */
	@Suppress("KDocUnresolvedReference")
	fun revokeDangerousPermission(packageName: String, permissionName: String) {
		val iPmClass: Class<*>
		val iPmStub: Class<*>
		val asInterfaceMethod: Method
		val revokeRuntimePermissionMethod: Method
		val iPmInstance: Any?

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			iPmClass = Class.forName(CLASS_NAME_PERMISSION_MANAGER)
			iPmStub = Class.forName(CLASS_NAME_PERMISSION_MANAGER_STUB)
			asInterfaceMethod = iPmStub.getMethod("asInterface", IBinder::class.java)
			revokeRuntimePermissionMethod = iPmClass.getMethod(
				REVOKE_RUNTIME_PERMISSION,
				String::class.java, // package name
				String::class.java, // permission name
				Int::class.java, // user ID
				String::class.java // reason
			)
			iPmInstance = asInterfaceMethod.invoke(null,
				ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PERMISSION_SERVICE))
			)
			if (!isPermissionSystemFixed(packageName, permissionName) &&
				(packageName != BuildConfig.APPLICATION_ID && permissionName != SHIZUKU_PERMISSION_NAME)) {
				revokeRuntimePermissionMethod.invoke(iPmInstance, packageName, permissionName, USER_ID, null)
				debug(DEBUG_TAG, "Permission $permissionName revoked for package $packageName")
			}
		}
		else {
			iPmClass = Class.forName(CLASS_NAME_PACKAGE_MANAGER)
			iPmStub = Class.forName(CLASS_NAME_PACKAGE_MANAGER_STUB)
			asInterfaceMethod = iPmStub.getMethod("asInterface", IBinder::class.java)
			revokeRuntimePermissionMethod = iPmClass.getMethod(
				REVOKE_RUNTIME_PERMISSION,
				String::class.java, // package name
				String::class.java, // permission name
				Int::class.java // user ID
			)
			iPmInstance = asInterfaceMethod.invoke(null,
				ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PACKAGE_SERVICE))
			)
			if (!isPermissionSystemFixed(packageName, permissionName) &&
				(packageName != BuildConfig.APPLICATION_ID && permissionName != SHIZUKU_PERMISSION_NAME)) {
				revokeRuntimePermissionMethod.invoke(iPmInstance, packageName, permissionName, USER_ID)
				debug(DEBUG_TAG, "Permission $permissionName revoked for package $packageName")
			}
		}
	}

	/**
	 * Since Android 11 (R - API level 30) the method **[IPackageManager.getPermissionFlags]** has been removed
	 * from IPackageManager.aidl and added to IPermissionManager.aidl.
	 * @see <a href="https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/permission/IPermissionManager.aidl#45">IPermissionManager.getPermissionFlags</a>
	 * Moreover, the order of the parameters for packageName and permissionName was swapped.
	 */
	@Suppress("KDocUnresolvedReference")
	private fun isPermissionSystemFixed(packageName: String, permissionName: String): Boolean {
		val iPmClass: Class<*>
		val iPmStub: Class<*>
		val asInterfaceMethod: Method
		val getPermissionFlagsMethod: Method
		val iPmInstance: Any?
		val flags: Int

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			iPmClass = Class.forName(CLASS_NAME_PERMISSION_MANAGER)
			iPmStub = Class.forName(CLASS_NAME_PERMISSION_MANAGER_STUB)
			asInterfaceMethod = iPmStub.getMethod("asInterface", IBinder::class.java)
			getPermissionFlagsMethod = iPmClass.getMethod(
				"getPermissionFlags",
				String::class.java, // package name
				String::class.java, // permission name
				Int::class.java // user ID
			)
			iPmInstance = asInterfaceMethod.invoke(null,
				ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PERMISSION_SERVICE))
			)
			flags = getPermissionFlagsMethod.invoke(iPmInstance, packageName, permissionName, USER_ID) as Int
		}
		else {
			iPmClass = Class.forName(CLASS_NAME_PACKAGE_MANAGER)
			iPmStub = Class.forName(CLASS_NAME_PACKAGE_MANAGER_STUB)
			asInterfaceMethod = iPmStub.getMethod("asInterface", IBinder::class.java)
			getPermissionFlagsMethod = iPmClass.getMethod(
				"getPermissionFlags",
				String::class.java, // permission name
				String::class.java, // package name
				Int::class.java // user ID
			)
			iPmInstance = asInterfaceMethod.invoke(null,
				ShizukuBinderWrapper(SystemServiceHelper.getSystemService(PACKAGE_SERVICE))
			)
			flags = getPermissionFlagsMethod.invoke(iPmInstance, permissionName, packageName, USER_ID) as Int
		}
		return flags and FLAG_PERMISSION_SYSTEM_FIXED != 0
	}

	private const val CLASS_NAME_PACKAGE_MANAGER = "android.content.pm.IPackageManager"
	private const val CLASS_NAME_PACKAGE_MANAGER_STUB = "android.content.pm.IPackageManager\$Stub"
	private const val PACKAGE_SERVICE = "package"
	private const val CLASS_NAME_PERMISSION_MANAGER = "android.permission.IPermissionManager"
	private const val CLASS_NAME_PERMISSION_MANAGER_STUB = "android.permission.IPermissionManager\$Stub"
	private const val PERMISSION_SERVICE = "permission"
	private const val GRANT_RUNTIME_PERMISSION = "grantRuntimePermission"
	private const val REVOKE_RUNTIME_PERMISSION = "revokeRuntimePermission"
	private const val USER_ID = 0
	private const val DEBUG_TAG = "RESULT"
	private const val FLAG_PERMISSION_SYSTEM_FIXED = 1 shl 4
	private const val SHIZUKU_PERMISSION_NAME = "moe.shizuku.manager.permission.API_V23"
}