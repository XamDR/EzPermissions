package com.maxdr.ezpermss.core

data class DangerousPermissionInfo(
	val realName: String,
	val name: String,
	val summary: String,
	val granted: Boolean = false
)

data class NormalPermissionInfo(val name: String, val summary: String)