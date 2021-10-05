package com.maxdr.ezpermss.ui.permissions.service

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class ForegroundServiceHandler(private val context: Context) {

	fun onClick() {
		val request = OneTimeWorkRequestBuilder<DangerousPermissionWorker>().build()
		WorkManager.getInstance(context).enqueue(request)
	}
}