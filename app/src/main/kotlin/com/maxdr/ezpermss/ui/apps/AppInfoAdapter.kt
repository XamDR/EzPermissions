package com.maxdr.ezpermss.ui.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.databinding.AppRowLayoutBinding

class AppInfoAdapter(private val userInstalledApps: List<AppInfo>) : RecyclerView.Adapter<AppInfoAdapter.AppViewHolder>() {

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
		return AppViewHolder(binding)
	}

	override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
		val newsItem = userInstalledApps[position]
		holder.bind(newsItem)
	}

	override fun getItemCount() = userInstalledApps.size
}