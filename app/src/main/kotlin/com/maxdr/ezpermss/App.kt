package com.maxdr.ezpermss

import android.app.Application
import com.maxdr.ezpermss.data.AppRepository

@Suppress("unused")
class App : Application() {

	override fun onCreate() {
		super.onCreate()
		AppRepository.initialize(this)
	}
}