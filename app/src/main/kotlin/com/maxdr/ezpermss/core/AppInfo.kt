package com.maxdr.ezpermss.core

import android.graphics.drawable.Drawable

data class AppInfo(
	val packageName: String,
	val packageIcon: Drawable,
	val packageNumberPermissions: Int)