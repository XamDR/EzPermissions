package com.maxdr.ezpermss.core

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class DangerousPermissionInfo(
	val name: String,
	@ColumnInfo(name = "simple_name") val simpleName: String,
	val summary: String,
	@ColumnInfo(name = "protection_level") val protectionLevel: Int,
	val granted: Boolean,
	val favorite: Boolean,
	@ColumnInfo(name = "app_id", index = true) var appId: String,
	@PrimaryKey(autoGenerate = true) val id: Long = 0) : Parcelable

data class NonDangerousPermissionInfo(
	val name: String,
	val simpleName: String,
	val summary: String,
	val protectionLevel: Int)
