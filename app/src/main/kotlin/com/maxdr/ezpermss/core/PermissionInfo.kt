package com.maxdr.ezpermss.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PermissionInfo(
	val name: String,
	val simpleName: String,
	val summary: String,
	val protectionLevel: Int,
	val granted: Boolean = false) : Parcelable
