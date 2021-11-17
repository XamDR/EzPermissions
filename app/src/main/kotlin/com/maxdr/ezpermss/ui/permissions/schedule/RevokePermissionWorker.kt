package com.maxdr.ezpermss.ui.permissions.schedule

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.core.PermissionHelper

class RevokePermissionWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

	override suspend fun doWork(): Result {
		revokeRuntimePermission()
		return Result.success()
	}

	private suspend fun revokeRuntimePermission() {
		val packageName = inputData.getString("PACKAGE_NAME")
		val permissionName = inputData.getString("PERMISSION_NAME")

		if (packageName != null && permissionName != null) {
			PermissionHelper.revokeDangerousPermission(packageName, permissionName)
			AppRepository.Instance.updateDangerousPermissionInfo(packageName, permissionName, false)
		}
	}
}