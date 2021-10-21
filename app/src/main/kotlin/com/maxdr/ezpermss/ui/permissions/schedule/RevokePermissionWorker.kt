package com.maxdr.ezpermss.ui.permissions.schedule

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.maxdr.ezpermss.ui.permissions.PermissionHelper

class RevokePermissionWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

	override fun doWork(): Result {
		revokeRuntimePermission(applicationContext)
		return Result.success()
	}

	private fun revokeRuntimePermission(context: Context) {
		val packageName = inputData.getString("PACKAGE_NAME")
		val permissionName = inputData.getString("PERMISSION_NAME")

		if (packageName != null && permissionName != null) {
			PermissionHelper.revokeDangerousPermission(context, packageName, permissionName)
		}
	}
}