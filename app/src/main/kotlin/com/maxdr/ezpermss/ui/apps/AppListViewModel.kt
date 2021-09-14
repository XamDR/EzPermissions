package com.maxdr.ezpermss.ui.apps

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxdr.ezpermss.core.AppInfo

class AppListViewModel(private val app: Application) : AndroidViewModel(app) {

	init {
		getIntalledApps()
	}

	val appInfoLiveData: LiveData<List<AppInfo>> = getIntalledApps()

	private fun getIntalledApps(): LiveData<List<AppInfo>> {
		val appInfoList = mutableListOf<AppInfo>()
		val pm = app.applicationContext.packageManager
		val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

		for (packageInfo in packages) {
			val drawableIcon = pm.getApplicationIcon(packageInfo.packageName)
			val packageName = pm.getApplicationLabel(packageInfo).toString()
			val icon = pm.getApplicationInfo(packageInfo.packageName, 0).icon
			val permissions: Array<String>? = pm.getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS).requestedPermissions

			// If icon == 0 it means the app doesn't have a custom icon
			// and instead uses the default application icon
			if (icon != 0 && pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
				appInfoList.add(AppInfo(
					packageName = packageName,
					packageIcon = drawableIcon,
					packageNumberPermissions = permissions?.size ?: 0))
			}
		}
		appInfoList.sortBy { it.packageName }
		return MutableLiveData(appInfoList)
	}
}