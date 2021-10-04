package com.maxdr.ezpermss.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DangerousPermissionInfo(
	val realName: String,
	val name: String,
	val summary: String,
	val granted: Boolean = false
) : Parcelable

data class NormalPermissionInfo(val name: String, val summary: String)