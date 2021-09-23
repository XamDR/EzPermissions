package com.maxdr.ezpermss.core

data class DangerousPermissionInfo(val name: String, val summary: String, var granted: Boolean = false)

data class NormalPermissionInfo(val name: String, val summary: String)