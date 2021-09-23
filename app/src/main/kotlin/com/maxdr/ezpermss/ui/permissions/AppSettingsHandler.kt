package com.maxdr.ezpermss.ui.permissions

import android.content.Context
import com.maxdr.ezpermss.util.openAppSystemSettings

class AppSettingsHandler(private val context: Context) {

	fun openAppSettings(packageName: String) = context.openAppSystemSettings(packageName)
}