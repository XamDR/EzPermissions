package com.maxdr.ezpermss.ui.permissions.schedule

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.maxdr.ezpermss.util.debug
import com.topjohnwu.superuser.Shell

class RevokePermissionWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

	override fun doWork(): Result {
		revokeRuntimePermission()
		return Result.success()
	}

	private fun revokeRuntimePermission() {
		val packageName = inputData.getString("PACKAGE_NAME")
		val permissionName = inputData.getString("PERMISSION_NAME")

		if (packageName != null && permissionName != null) {
			val result = Shell.su("pm revoke $packageName $permissionName").exec()
			debug("RESULT", result.err)
		}
	}
}