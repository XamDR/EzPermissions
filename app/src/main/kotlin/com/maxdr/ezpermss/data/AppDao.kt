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
	fun getAppInfoByName(): Flow<List<AppInfo>>

	@Insert
	suspend fun insertAppInfo(appInfo: AppInfo)

	@Query("SELECT * FROM DangerousPermissionInfo where app_id=:appFullName")
	fun getDangerousPermissionInfoForApp(appFullName: String): Flow<List<DangerousPermissionInfo>>

	@Query("SELECT * FROM DangerousPermissionInfo where app_id=:appFullName ORDER BY simple_name")
	fun getDangerousPermissionInfoForAppByName(appFullName: String): Flow<List<DangerousPermissionInfo>>

	@Insert
	suspend fun insertDangerousPermissionInfo(dangerousPermissionInfo: DangerousPermissionInfo)

	@Query("UPDATE DangerousPermissionInfo SET granted=:granted WHERE app_id=:packageName AND name=:permissionName")
	suspend fun updateDangerousPermissionInfo(packageName: String, permissionName: String, granted: Boolean)

	@Query("UPDATE DangerousPermissionInfo SET favorite=:favorite WHERE app_id=:packageName AND name=:permissionName")
	suspend fun updateDangerousPermissionFavoriteInfo(packageName: String, permissionName: String, favorite: Boolean)

	@Query("SELECT favorite FROM DangerousPermissionInfo WHERE app_id=:packageName AND name=:permissionName")
	suspend fun getDangerousPermissionFavoriteInfo(packageName: String, permissionName: String) : Boolean?

	@Query("DELETE FROM ApplicationInfo WHERE full_name=:packageName")
	suspend fun removeAppInfo(packageName: String)
}
