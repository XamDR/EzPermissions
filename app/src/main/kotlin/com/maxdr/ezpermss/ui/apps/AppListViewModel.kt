package com.maxdr.ezpermss.ui.apps

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxdr.ezpermss.core.App
import com.maxdr.ezpermss.core.AppInfo

class AppListViewModel(private val app: Application) : AndroidViewModel(app) {

	val appInfoLiveData: LiveData<List<App>> = getIntalledApps()

	private fun getIntalledApps(): LiveData<List<App>> {
		val apps = mutableListOf<App>()
		val pm = app.applicationContext.packageManager
		val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

		for (packageInfo in packages) {
			val packageFullName = packageInfo.packageName
			val icon = pm.getApplicationInfo(packageFullName, 0).icon

			// If icon == 0 it means the app doesn't have a custom icon
			// and instead uses the default application icon
			// Also if pm.getLaunchIntentForPackage(packageFullName) returns null
			// it means the app doesn't have a proper UI (main activity)
			if (icon != 0 && pm.getLaunchIntentForPackage(packageFullName) != null) {
				val packageName = pm.getApplicationLabel(packageInfo).toString()
				val drawableIcon = pm.getApplicationIcon(packageFullName)
				val permissions = pm.getPackageInfo(packageFullName, PackageManager.GET_PERMISSIONS).requestedPermissions

				apps.add(App(
					info = AppInfo(
						packageName = packageName,
						packageFullName = packageFullName,
						packageNumberPermissions = permissions?.size ?: 0
					),
					icon = drawableIcon))
			}
		}
		apps.sortBy { it.info.packageName }
		return MutableLiveData(apps)
	}
}