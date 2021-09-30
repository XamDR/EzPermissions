package com.maxdr.ezpermss.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PermissionInfo
import android.net.Uri
import android.provider.Settings
import android.text.SpannableString
import android.text.style.BulletSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.maxdr.ezpermss.MainActivity
import java.util.Locale

val String.Companion.Empty: String
	get() = ""

fun String.toTitleCase() = this.replaceFirstChar {
	if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

val Fragment.mainActivity: MainActivity
	get() = requireActivity() as? MainActivity
		?: throw IllegalStateException("The activity this fragment is attached to does not extend MainActivity.")

fun FragmentManager.instantiate(className: String) =
	fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), className)

fun List<String>.toBulletedList(): CharSequence {
	return SpannableString(this.joinToString("\n")).apply {
		this@toBulletedList.foldIndexed(0) { index, acc, span ->
			val end = acc + span.length + if (index != this@toBulletedList.size - 1) 1 else 0
			this.setSpan(BulletSpan(16), acc, end, 0)
			end
		}
	}
}

fun Context.openAppSystemSettings(packageName: String) {
	try {
		startActivity(Intent().apply {
			action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
			data = Uri.fromParts("package", packageName, null)
		})
	}
	catch (e: ActivityNotFoundException) { }
}

@Suppress("DEPRECATION")
fun protectionToString(level: Int): String {
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
	return protectionLevel
}