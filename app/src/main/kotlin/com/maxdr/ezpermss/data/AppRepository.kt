package com.maxdr.ezpermss.data

import android.content.Context
import androidx.room.Room
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.core.PermissionInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

class AppRepository(context: Context) {

	fun getAppInfoPermissions()
		= appDao.getAppInfoPermissions().flowOn(Dispatchers.Main).conflate()

	suspend fun insertAppInfoPermissions(app: AppInfo, permissions: List<PermissionInfo>)
		= appDao.insertAppInfoPermissions(app, permissions)

	private val database = Room
		.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
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