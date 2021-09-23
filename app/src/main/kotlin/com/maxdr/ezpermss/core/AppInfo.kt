package com.maxdr.ezpermss.core

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppInfo(
	val packageName: String,
	val packageFullName: String,
	val packageNumberPermissions: Int) : Parcelable

data class App(
	val info: AppInfo,
	val icon: Drawable
)