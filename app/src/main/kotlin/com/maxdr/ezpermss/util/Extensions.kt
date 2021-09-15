package com.maxdr.ezpermss.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.maxdr.ezpermss.MainActivity

val Fragment.mainActivity: MainActivity
	get() = requireActivity() as? MainActivity
		?: throw IllegalStateException("The activity this fragment is attached to does not extend MainActivity.")

fun FragmentManager.instantiate(className: String) =
	fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), className)