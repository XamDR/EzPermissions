package com.maxdr.ezpermss.ui.helpers

import android.os.Bundle

interface NavigationService {
	fun navigate(className: String, args: Bundle? = null)
}