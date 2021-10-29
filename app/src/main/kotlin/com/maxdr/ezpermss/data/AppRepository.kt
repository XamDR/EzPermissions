package com.maxdr.ezpermss.data

import android.content.Context
import androidx.room.Room
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

class AppRepository(context: Context) {

	fun getAppInfo() = appDao.getAppInfo().flowOn(Dispatchers.Main).conflate()

	suspend fun insertAppInfo(app: AppInfo) = appDao.insertAppInfo(app)

	suspend fun removeAppInfo(packageName: String) = appDao.removeAppInfo(packageName)

	suspend fun deleteTableDangerousPermissionInfo() {
		appDao.deleteTableDangerousPermissionInfo()
		database.resetPointer()
	}

	suspend fun updateDangerousPermissionInfo(packageName: String, permissionName: String, granted: Boolean)
		= appDao.updateDangerousPermissionInfo(packageName, permissionName, granted)

	fun getDangerousPermissionInfo(packageName: String)
		= appDao.getDangerousPermissionInfoForApp(packageName).flowOn(Dispatchers.Main).conflate()

	suspend fun insertDangerousPermissionInfo(dangerousPermissionInfo: DangerousPermissionInfo)
		= appDao.insertDangerousPermissionInfo(dangerousPermissionInfo)

	private val database =
		Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
			.build()

	private val appDao = database.appDao()

	companion object {
		private const val DATABASE_NAME = "ezpermss-database"

		val Instance: AppRepository
			get() = instance ?: throw IllegalStateException("Repository service must be initialized")
		private var instance: AppRepository? = null

		fun initialize(context: Context) {
			if (instance == null) {
				instance = AppRepository(context)
			}
		}
	}
}