package com.maxdr.ezpermss.ui.permissions

import android.content.Context
import android.widget.Toast
import com.maxdr.ezpermss.util.debug
import com.topjohnwu.superuser.Shell

object PermissionHelper {

	fun grantDangerousPermission(context: Context, packageName: String, permissionName: String) {
		if (Shell.rootAccess()) {
			val result = Shell.su("pm grant $packageName $permissionName").exec()
			debug("RESULT", result.err)
		}
		else {
			Toast.makeText(context, "No Root", Toast.LENGTH_SHORT).show()
		}
	}

	fun revokeDangerousPermission(context: Context, packageName: String, permissionName: String) {
		if (Shell.rootAccess()) {
			val result = Shell.su("pm revoke $packageName $permissionName").exec()
			debug("RESULT", result.err)
		}
		else {
			Toast.makeText(context,"No Root", Toast.LENGTH_SHORT).show()
		}
	}

	fun grantDangerousPermissions(packageName: String, permissions: String) {
		val command = "PACKAGE='${packageName}'; IN='${permissions}'; for i in \$(IFS=',';echo \$IN); do pm grant \$PACKAGE \$i; done;"
		val result = Shell.su(command).exec()
		debug("RESULT", result.err)
	}

	fun revokeDangerousPermissions(packageName: String, permissions: String) {
		val command = "PACKAGE='${packageName}'; IN='${permissions}'; for i in \$(IFS=',';echo \$IN); do pm revoke \$PACKAGE \$i; done;"
		val result = Shell.su(command).exec()
		debug("RESULT", result.err)
	}
}