package com.maxdr.ezpermss

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maxdr.ezpermss.core.PackageManagerHelper
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.databinding.ActivityMainBinding
import com.maxdr.ezpermss.ui.apps.PackageReceiver
import com.maxdr.ezpermss.ui.helpers.NavigationService
import com.maxdr.ezpermss.util.debug
import com.maxdr.ezpermss.util.instantiate
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationService {

	private lateinit var binding: ActivityMainBinding
	private val packageReceiver = PackageReceiver { appFullName, added ->
		lifecycleScope.launch {
			if (added) {
				PackageManagerHelper(this@MainActivity).insertAppInfo(appFullName)
			}
			else {
				AppRepository.Instance.removeAppInfo(appFullName)
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		setSupportActionBar(binding.toolbar)
		registerReceiver(packageReceiver, IntentFilter().apply {
			addAction(Intent.ACTION_PACKAGE_ADDED)
			addAction(Intent.ACTION_PACKAGE_REMOVED)
			addDataScheme("package")
		})
	}

	override fun onStart() {
		super.onStart()
		insertDangerousPermisionInfo()
	}

	override fun onStop() {
		super.onStop()
		lifecycleScope.launch {
			AppRepository.Instance.deleteTableDangerousPermissionInfo()
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		unregisterReceiver(packageReceiver)
	}

	override fun navigate(className: String, args: Bundle?) {
		val fragment = supportFragmentManager.instantiate(className).apply { arguments = args }
		supportFragmentManager.beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.addToBackStack(null)
			.commit()
	}

	private fun insertDangerousPermisionInfo() {
		lifecycleScope.launch {
			PackageManagerHelper(this@MainActivity).insertDangerousPermissions()
			debug("TAG", "Insertion done")
		}
	}
}