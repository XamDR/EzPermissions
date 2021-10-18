package com.maxdr.ezpermss.ui.apps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.data.AppRepository

class AppListViewModel : ViewModel() {

	val appInfoLiveData: LiveData<List<AppInfo>> = AppRepository.Instance.getAppInfo().asLiveData()
}