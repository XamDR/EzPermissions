package com.maxdr.ezpermss.ui.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.databinding.AppRowLayoutBinding
import com.maxdr.ezpermss.ui.helpers.NavigationService
import com.maxdr.ezpermss.ui.permissions.PermissionDetailFragment

class AppInfoAdapter(private val userInstalledApps: List<AppInfo>,
					 private val navigator: NavigationService) : RecyclerView.Adapter<AppInfoAdapter.AppViewHolder>() {

	class AppViewHolder(private val binding: AppRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(appInfo: AppInfo) {
			binding.apply {
				this.appInfo = appInfo
				executePendingBindings()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
		val binding = AppRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		binding.next.setOnClickListener { navigateToPermissionDetails() }
		return AppViewHolder(binding)
	}

	override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
		val newsItem = userInstalledApps[position]
		holder.bind(newsItem)
	}

	override fun getItemCount() = userInstalledApps.size

	private fun navigateToPermissionDetails() = navigator.navigate(PermissionDetailFragment::class.java.name)
}