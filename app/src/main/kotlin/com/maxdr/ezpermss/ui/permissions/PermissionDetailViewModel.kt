package com.maxdr.ezpermss.ui.permissions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.maxdr.ezpermss.core.PackageManagerHelper
import com.maxdr.ezpermss.core.PermissionInfo

class PermissionDetailViewModel(private val app: Application,
								val appFullName: String) : AndroidViewModel(app) {

	val hasNonDangerousPermissions = MutableLiveData(false)

	val hasDangerousPermissions = MutableLiveData(false)

	val nonDangerousPermissions: LiveData<List<PermissionInfo>> = fetchNonDangerousPermissions(appFullName)

	val dangerousPermissions: LiveData<List<PermissionInfo>> = fetchDangerousPermissions(appFullName)

	private fun fetchDangerousPermissions(appFullName: String): LiveData<List<PermissionInfo>> {
		return PackageManagerHelper(app.applicationContext).fetchDangerousPermissions(appFullName).asLiveData()
	}

	private fun fetchNonDangerousPermissions(appFullName: String): LiveData<List<PermissionInfo>> {
		return PackageManagerHelper(app.applicationContext).fetchNonDangerousPermissions(appFullName).asLiveData()
	}
}