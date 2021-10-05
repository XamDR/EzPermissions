package com.maxdr.ezpermss.ui.permissions.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.maxdr.ezpermss.ui.helpers.NotificationHelper
import kotlinx.coroutines.delay

class DangerousPermissionWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

	override suspend fun doWork(): Result {
		setForeground(createForegroundInfo())
		delay(20000)
		return Result.success()
	}

	private fun createForegroundInfo(): ForegroundInfo {
		val notification = NotificationHelper.createNotification(applicationContext, id)
		return ForegroundInfo(NOTIFICATION_ID, notification)
	}

	companion object {
		private const val NOTIFICATION_ID = 1
	}
}