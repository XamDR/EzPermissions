package com.maxdr.ezpermss.ui.permissions

import android.content.pm.PermissionInfo.PROTECTION_DANGEROUS
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.maxdr.ezpermss.core.PermissionInfo
import com.maxdr.ezpermss.data.AppRepository

class PermissionDetailViewModel(val appFullName: String) : ViewModel() {

	val hasNonDangerousPermissions = MutableLiveData(false)

	val hasDangerousPermissions = MutableLiveData(false)

	val nonDangerousPermissions: LiveData<List<PermissionInfo>> = fetchNormalPermissions(appFullName)

	val dangerousPermissions: LiveData<List<PermissionInfo>> = fetchDangerousPermissions(appFullName)

	private fun fetchDangerousPermissions(appFullName: String): LiveData<List<PermissionInfo>> {
		return AppRepository.Instance.getPermissionInfoForApp(appFullName).asLiveData().map { list ->
			list.first { it.appInfo.fullName == appFullName }.permissions
				.filter { pi -> pi.protectionLevel == PROTECTION_DANGEROUS }
		}
	}

	private fun fetchNormalPermissions(appFullName: String): LiveData<List<PermissionInfo>> {
		return AppRepository.Instance.getPermissionInfoForApp(appFullName).asLiveData().map { list ->
			list.first { it.appInfo.fullName == appFullName }.permissions
				.filter { pi -> pi.protectionLevel != PROTECTION_DANGEROUS }
		}
	}
}