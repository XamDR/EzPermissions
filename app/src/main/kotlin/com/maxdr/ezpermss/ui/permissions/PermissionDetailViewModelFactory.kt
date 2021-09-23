package com.maxdr.ezpermss.ui.permissions

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PermissionDetailViewModelFactory(private val app: Application,
									   private val packageFullName: String) : ViewModelProvider.Factory {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>) = PermissionDetailViewModel(app, packageFullName) as T
}