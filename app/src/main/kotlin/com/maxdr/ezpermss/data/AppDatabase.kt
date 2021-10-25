package com.maxdr.ezpermss.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.core.DangerousPermissionInfo

@Database(entities = [AppInfo::class, DangerousPermissionInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

	abstract fun appDao(): AppDao
}