package com.maxdr.ezpermss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maxdr.ezpermss.ui.helpers.NavigationService
import com.maxdr.ezpermss.util.instantiate

class MainActivity : AppCompatActivity(R.layout.activity_main), NavigationService {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}

	override fun navigate(className: String, args: Bundle?) {
		val fragment = supportFragmentManager.instantiate(className).apply { arguments = args }
		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.addToBackStack(null)
			.commit()
	}
}