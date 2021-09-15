package com.maxdr.ezpermss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maxdr.ezpermss.databinding.ActivityMainBinding
import com.maxdr.ezpermss.ui.helpers.NavigationService
import com.maxdr.ezpermss.util.instantiate

class MainActivity : AppCompatActivity(), NavigationService {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		setSupportActionBar(binding.toolbar)
	}

	override fun navigate(className: String, args: Bundle?) {
		val fragment = supportFragmentManager.instantiate(className).apply { arguments = args }
		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.addToBackStack(null)
			.commit()
	}
}