package com.maxdr.ezpermss

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maxdr.ezpermss.core.PackageManagerHelper
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.databinding.ActivityMainBinding
import com.maxdr.ezpermss.ui.apps.PackageReceiver
import com.maxdr.ezpermss.ui.helpers.NavigationService
import com.maxdr.ezpermss.util.instantiate
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuProvider

class MainActivity : AppCompatActivity(), NavigationService, Shizuku.OnRequestPermissionResultListener {

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
		checkShizuku()
		registerReceiver(packageReceiver, IntentFilter().apply {
			addAction(Intent.ACTION_PACKAGE_ADDED)
			addAction(Intent.ACTION_PACKAGE_REMOVED)
			addDataScheme("package")
		})
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

	override fun onRequestPermissionsResult(requestCode: Int,
											permissions: Array<out String>,
											grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		permissions.forEachIndexed { index, permission ->
			if (permission == ShizukuProvider.PERMISSION) {
				onRequestPermissionResult(requestCode, grantResults[index])
			}
		}
	}

	override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
		val isGranted = grantResult == PackageManager.PERMISSION_GRANTED
		if (!isGranted) {
			requestShizuku()
		}
	}

	private fun checkShizuku() {
		val isGranted = if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) {
			checkSelfPermission(ShizukuProvider.PERMISSION) == PackageManager.PERMISSION_GRANTED
		}
		else {
			Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
		}
		if (!isGranted) {
			requestShizuku()
		}
	}

	private fun requestShizuku() {
		if (Shizuku.isPreV11() || Shizuku.getVersion() < 11) {
			requestPermissions(arrayOf(ShizukuProvider.PERMISSION), SHIZUKU_CODE)
		}
		else {
			Shizuku.requestPermission(SHIZUKU_CODE)
		}
	}

	companion object {
		private const val SHIZUKU_CODE = 1
	}
}