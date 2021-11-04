package com.maxdr.ezpermss.ui.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.databinding.AppRowLayoutBinding
import com.maxdr.ezpermss.ui.helpers.NavigationService
import com.maxdr.ezpermss.ui.permissions.PermissionDetailFragment
import com.maxdr.ezpermss.util.setOnClickListener

class AppInfoAdapter(private val navigator: NavigationService) : ListAdapter<AppInfo, AppInfoAdapter.AppViewHolder>(AppInfoCallback()) {

	inner class AppViewHolder(private val binding: AppRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(app: AppInfo) {
			binding.apply {
				this.app = app
				executePendingBindings()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
		val binding = AppRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return AppViewHolder(binding).apply {
			setOnClickListener { position, _ -> navigateToPermissionDetails(position) }
		}
	}

	override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
		val appInfo = getItem(position)
		holder.bind(appInfo)
	}

	private fun navigateToPermissionDetails(position: Int) {
		val appInfo = getItem(position)
		val args = bundleOf("info" to appInfo)
		navigator.navigate(PermissionDetailFragment::class.java.name, args)
	}

	private class AppInfoCallback : DiffUtil.ItemCallback<AppInfo>() {

		override fun areItemsTheSame(oldAppInfo: AppInfo, newAppInfo: AppInfo) = oldAppInfo.fullName == newAppInfo.fullName

		override fun areContentsTheSame(oldAppInfo: AppInfo, newAppInfo: AppInfo) = oldAppInfo == newAppInfo
	}
}