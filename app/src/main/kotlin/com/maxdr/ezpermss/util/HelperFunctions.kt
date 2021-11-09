package com.maxdr.ezpermss.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PermissionInfo
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

fun debug(tag: String, msg: Any?) = Log.d(tag, msg.toString())

object Converter {
	@Suppress("DEPRECATION")
	@JvmStatic fun protectionToString(level: Int): String {
		var protectionLevel = String.Empty

		when (level and PermissionInfo.PROTECTION_MASK_BASE) {
			PermissionInfo.PROTECTION_DANGEROUS -> protectionLevel = "dangerous"
			PermissionInfo.PROTECTION_NORMAL -> protectionLevel = "normal"
			PermissionInfo.PROTECTION_SIGNATURE -> protectionLevel = "signature"
			PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM -> protectionLevel = "signatureOrSystem"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_PRIVILEGED != 0) {
			protectionLevel += "|privileged"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_DEVELOPMENT != 0) {
			protectionLevel += "|development"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_APPOP != 0) {
			protectionLevel += "|appop"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_PRE23 != 0) {
			protectionLevel += "|pre23"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_INSTALLER != 0) {
			protectionLevel += "|installer"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_VERIFIER != 0) {
			protectionLevel += "|verifier"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_PREINSTALLED != 0) {
			protectionLevel += "|preinstalled"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_SETUP != 0) {
			protectionLevel += "|setup"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_INSTANT != 0) {
			protectionLevel += "|instant"
		}
		if (level and PermissionInfo.PROTECTION_FLAG_RUNTIME_ONLY != 0) {
			protectionLevel += "|runtime"
		}
		if (level and PermissionInfo.FLAG_COSTS_MONEY != 0) {
			protectionLevel += "|costs money"
		}
		return protectionLevel
	}
}

fun setNightMode(preferences: SharedPreferences) {
	when (preferences.getString("app_theme", "-1")?.toInt()) {
		0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
		-1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
	}
}

fun installShizuku(fragment: Fragment) {
	val uri = Uri.parse("${URI_MARKET}${SHIZUKU}")
	val goToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
		addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
				Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
				Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
	}
	try {
		fragment.startActivity(goToMarket)
	}
	catch (e: ActivityNotFoundException) {
		fragment.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("${URI_PLAY_STORE}${SHIZUKU}")))
	}
}

private const val SHIZUKU = "moe.shizuku.privileged.api"
private const val URI_MARKET = "market://details?id="
private const val URI_PLAY_STORE = "http://play.google.com/store/apps/details?id="