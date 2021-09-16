package com.maxdr.ezpermss.util

import android.text.SpannableString
import android.text.style.BulletSpan
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.maxdr.ezpermss.MainActivity

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