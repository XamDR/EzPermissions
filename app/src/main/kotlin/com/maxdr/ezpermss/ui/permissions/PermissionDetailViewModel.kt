package com.maxdr.ezpermss.ui.permissions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.core.NonDangerousPermissionInfo
import com.maxdr.ezpermss.core.PackageManagerHelper
import com.maxdr.ezpermss.data.AppRepository

class PermissionDetailViewModel(private val app: Application,
								val appFullName: String) : AndroidViewModel(app) {

	val hasNonDangerousPermissions = MutableLiveData(false)

	val hasDangerousPermissions = MutableLiveData(false)

	val nonDangerousPermissions: LiveData<List<NonDangerousPermissionInfo>> = fetchNonDangerousPermissions(appFullName)

	val dangerousPermissions: LiveData<List<DangerousPermissionInfo>> = fetchDangerousPermissions(appFullName)

	val dangerousPermissionsFromDb: LiveData<List<DangerousPermissionInfo>> = fetchDangerousPermissionsFromDatabase(appFullName)

	private fun fetchDangerousPermissions(appFullName: String): LiveData<List<DangerousPermissionInfo>> {
		return PackageManagerHelper(app.applicationContext).fetchDangerousPermissions(appFullName).asLiveData()
	}

	private fun fetchDangerousPermissionsFromDatabase(appFullName: String): LiveData<List<DangerousPermissionInfo>> {
		return AppRepository.Instance.getDangerousPermissionInfo(appFullName).asLiveData()
	}

	private fun fetchNonDangerousPermissions(appFullName: String): LiveData<List<NonDangerousPermissionInfo>> {
		return PackageManagerHelper(app.applicationContext).fetchNonDangerousPermissions(appFullName).asLiveData()
	}
}