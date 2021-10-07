package com.maxdr.ezpermss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.core.AppInfoPermissions
import com.maxdr.ezpermss.core.PermissionInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

	@Transaction
	@Query("SELECT * FROM ApplicationInfo")
	fun getAppInfoPermissions(): Flow<List<AppInfoPermissions>>

	@Transaction
	suspend fun insertAppInfoPermissions(appInfo: AppInfo, permissions: List<PermissionInfo>) {
		val appId = insertAppInfo(appInfo)
		for (permission in permissions) {
			permission.appId = appId
			insertPermissionInfo(permission)
		}
	}

	@Insert
	suspend fun insertAppInfo(appInfo: AppInfo): Long

	@Insert
	suspend fun insertPermissionInfo(permissionInfo: PermissionInfo)
}
