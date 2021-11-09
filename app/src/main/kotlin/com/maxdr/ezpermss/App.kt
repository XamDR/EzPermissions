package com.maxdr.ezpermss

import android.app.Application
import android.content.Context
import com.maxdr.ezpermss.data.AppRepository
import me.weishu.reflection.Reflection

@Suppress("unused")
class App : Application() {

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
		Reflection.unseal(base)
	}

	override fun onCreate() {
		super.onCreate()
		AppRepository.initialize(this)
	}
}