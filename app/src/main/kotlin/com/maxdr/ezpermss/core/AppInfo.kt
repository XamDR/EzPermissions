package com.maxdr.ezpermss.core

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ApplicationInfo")
data class AppInfo @JvmOverloads constructor(
	val name: String,
	val fullName: String,
	val numberOfPermissions: Int,
	val drawableIconPath: String,
	@PrimaryKey(autoGenerate = true) var id: Long = 0
) : Parcelable
