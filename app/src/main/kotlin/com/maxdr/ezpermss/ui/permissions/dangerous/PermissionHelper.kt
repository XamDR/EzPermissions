package com.maxdr.ezpermss.ui.permissions.dangerous

import com.maxdr.ezpermss.util.debug
import com.topjohnwu.superuser.Shell

object PermissionHelper {

	fun grantDangerousPermission(packageName: String, permissionName: String) {
		if (Shell.rootAccess()) {
			val result = Shell.su("pm grant $packageName $permissionName").exec()
			debug("RESULT", result.err)
		}
	}

	fun revokeDangerousPermission(packageName: String, permissionName: String) {
		if (Shell.rootAccess()) {
			val result = Shell.su("pm revoke $packageName $permissionName").exec()
			debug("RESULT", result.err)
		}
	}
}