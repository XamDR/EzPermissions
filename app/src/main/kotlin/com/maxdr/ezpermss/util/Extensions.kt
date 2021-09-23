package com.maxdr.ezpermss.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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