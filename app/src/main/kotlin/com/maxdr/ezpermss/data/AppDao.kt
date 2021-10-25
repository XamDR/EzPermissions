package com.maxdr.ezpermss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.maxdr.ezpermss.core.AppInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

	@Query("SELECT * FROM ApplicationInfo")
	fun getAppInfo(): Flow<List<AppInfo>>

	@Insert
	suspend fun insertAppInfo(appInfo: AppInfo)

	@Query("DELETE FROM ApplicationInfo WHERE fullName=:appFullName")
	suspend fun removeAppInfo(appFullName: String)
}
