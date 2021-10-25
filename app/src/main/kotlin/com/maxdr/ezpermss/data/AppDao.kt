package com.maxdr.ezpermss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

	@Query("SELECT * FROM ApplicationInfo")
	fun getAppInfo(): Flow<List<AppInfo>>

	@Insert
	suspend fun insertAppInfo(appInfo: AppInfo)

	@Query("SELECT * FROM DangerousPermissionInfo")
	fun getDangerousPermissionInfo(): Flow<List<DangerousPermissionInfo>>

	@Insert
	suspend fun insertDangerousPermissionInfo(dangerousPermissionInfo: DangerousPermissionInfo)

	@Query("DELETE FROM ApplicationInfo WHERE fullName=:appFullName")
	suspend fun removeAppInfo(appFullName: String)
}
