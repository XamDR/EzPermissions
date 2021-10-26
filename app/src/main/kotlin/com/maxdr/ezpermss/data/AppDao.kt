package com.maxdr.ezpermss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

	@Query("SELECT * FROM ApplicationInfo ORDER BY name")
	fun getAppInfo(): Flow<List<AppInfo>>

	@Insert
	suspend fun insertAppInfo(appInfo: AppInfo)

	@Query("SELECT * FROM DangerousPermissionInfo where app_id=:appFullName ORDER BY simple_name")
	fun getDangerousPermissionInfoForApp(appFullName: String): Flow<List<DangerousPermissionInfo>>

	@Insert
	suspend fun insertDangerousPermissionInfo(dangerousPermissionInfo: DangerousPermissionInfo)

	@Query("UPDATE DangerousPermissionInfo SET granted=:value WHERE app_id=:appFullName AND name=:permissionName")
	suspend fun updateDangerousPermissionInfo(appFullName: String, permissionName: String, value: Boolean)

	@Query("DELETE FROM DangerousPermissionInfo")
	suspend fun deleteTableDangerousPermissionInfo()

	@Query("DELETE FROM ApplicationInfo WHERE fullName=:appFullName")
	suspend fun removeAppInfo(appFullName: String)
}
