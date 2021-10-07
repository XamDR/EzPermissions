package com.maxdr.ezpermss.core

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppInfoPermissions(
	@Embedded val appInfo: AppInfo,
	@Relation(
		parentColumn = "id",
		entityColumn = "app_id"
	) val permissions: List<PermissionInfo>
) : Parcelable
