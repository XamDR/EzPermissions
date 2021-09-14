package com.maxdr.ezpermss.util

import androidx.fragment.app.FragmentManager

fun FragmentManager.instantiate(className: String) =
	fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), className)