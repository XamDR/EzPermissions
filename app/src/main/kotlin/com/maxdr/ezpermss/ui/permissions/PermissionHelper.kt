package com.maxdr.ezpermss.ui.permissions

import android.content.Context
import android.widget.Toast
import com.maxdr.ezpermss.util.debug
import com.topjohnwu.superuser.Shell

class PermissionHelper(private val context: Context) {

	fun grantDangerousPermission(packageFullName: String, permissionName: String) {
		if (Shell.rootAccess()) {
			val result = Shell.su("pm grant $packageFullName $permissionName").exec()
			debug("RESULT", result.err)
		}
		else {
			Toast.makeText(context, "No Root", Toast.LENGTH_SHORT).show()
		}
	}

	fun revokeDangerousPermission(packageFullName: String, permissionName: String) {
		if (Shell.rootAccess()) {
			val result = Shell.su("pm revoke $packageFullName $permissionName").exec()
			debug("RESULT", result.err)
		}
		else {
			Toast.makeText(context, "No Root", Toast.LENGTH_SHORT).show()
		}
	}
}