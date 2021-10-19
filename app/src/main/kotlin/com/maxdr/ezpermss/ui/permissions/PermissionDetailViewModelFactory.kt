package com.maxdr.ezpermss.ui.permissions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PermissionDetailViewModelFactory(private val appFullName: String) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>) = PermissionDetailViewModel(appFullName) as T
}