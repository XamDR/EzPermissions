package com.maxdr.ezpermss.ui.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.databinding.AppRowLayoutBinding
import com.maxdr.ezpermss.ui.helpers.NavigationService
import com.maxdr.ezpermss.ui.permissions.PermissionDetailFragment
import com.maxdr.ezpermss.util.setOnClickListener

class AppInfoAdapter(
	private val apps: List<AppInfo>,
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
			setOnClickListener { position, _ -> navigateToPermissionDetails(position) }
		}
	}

	override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
		val appInfo = apps[position]
		holder.bind(appInfo)
	}

	override fun getItemCount() = apps.size

	private fun navigateToPermissionDetails(position: Int) {
		val appInfo = apps[position]
		val args = bundleOf("info" to appInfo)
		navigator.navigate(PermissionDetailFragment::class.java.name, args)
	}
}