package com.maxdr.ezpermss.ui.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.databinding.AppRowLayoutBinding

class AppInfoAdapter(private val apps: List<AppInfo>) : RecyclerView.Adapter<AppInfoAdapter.AppViewHolder>() {

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
//			binding.next.setOnClickListener { navigateToPermissionDetails(adapterPosition) }
		}
	}

	override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
		val newsItem = apps[position]
		holder.bind(newsItem)
	}

	override fun getItemCount() = apps.size
}