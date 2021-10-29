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

	@Query("UPDATE DangerousPermissionInfo SET granted=:granted WHERE app_id=:packageName AND name=:permissionName")
	suspend fun updateDangerousPermissionInfo(packageName: String, permissionName: String, granted: Boolean)

	@Query("DELETE FROM DangerousPermissionInfo")
	suspend fun deleteTableDangerousPermissionInfo()

	@Query("DELETE FROM ApplicationInfo WHERE fullName=:packageName")
	suspend fun removeAppInfo(packageName: String)
}
