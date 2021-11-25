package com.maxdr.ezpermss.core

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ApplicationInfo")
data class AppInfo constructor(
	val name: String,
	@ColumnInfo(name = "full_name") @PrimaryKey val fullName: String,
	@ColumnInfo(name = "system_app") val systemApp: Boolean,
	val numberOfPermissions: Int,
	@ColumnInfo(name = "drawable_icon_path") val drawableIconPath: String) : Parcelable
