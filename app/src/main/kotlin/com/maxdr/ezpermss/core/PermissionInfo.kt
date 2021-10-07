package com.maxdr.ezpermss.core

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class PermissionInfo(
	val name: String,
	@ColumnInfo(name = "simple_name") val simpleName: String,
	val summary: String,
	@ColumnInfo(name = "protection_level") val protectionLevel: Int,
	val granted: Boolean = false,
	val modified: Boolean = false,
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	@ColumnInfo(name = "app_id", index = true) var appId: Long = 0
) : Parcelable
