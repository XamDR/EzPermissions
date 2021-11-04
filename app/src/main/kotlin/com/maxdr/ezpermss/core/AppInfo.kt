package com.maxdr.ezpermss.core

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ApplicationInfo")
data class AppInfo constructor(
	val name: String,
	@PrimaryKey val fullName: String,
	val numberOfPermissions: Int,
	val drawableIconPath: String
) : Parcelable
