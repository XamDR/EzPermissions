package com.maxdr.ezpermss.ui.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.core.AppInfoPermissions
import com.maxdr.ezpermss.databinding.AppRowLayoutBinding
import com.maxdr.ezpermss.ui.helpers.NavigationService
import com.maxdr.ezpermss.ui.permissions.PermissionDetailFragment

class AppInfoAdapter(
	private val apps: List<AppInfoPermissions>,
	private val navigator: NavigationService) : RecyclerView.Adapter<AppInfoAdapter.AppViewHolder>() {

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
			binding.next.setOnClickListener { navigateToPermissionDetails(bindingAdapterPosition) }
		}
	}

	override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
		val appInfo = apps[position].appInfo
		holder.bind(appInfo)
	}

	override fun getItemCount() = apps.size

	private fun navigateToPermissionDetails(position: Int) {
		val appInfoPermissions = apps[position]
		val args = bundleOf("info" to appInfoPermissions)
		navigator.navigate(PermissionDetailFragment::class.java.name, args)
	}
}