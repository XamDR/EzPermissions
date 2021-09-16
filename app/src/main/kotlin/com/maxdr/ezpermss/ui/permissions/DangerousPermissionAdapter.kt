package com.maxdr.ezpermss.ui.permissions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.databinding.DangerousPermissionRowLayoutBinding

class DangerousPermissionAdapter(private val dangerousPermissions: List<DangerousPermissionInfo>) :
	RecyclerView.Adapter<DangerousPermissionAdapter.DangerousPermissionViewHolder>() {

	inner class DangerousPermissionViewHolder(
		private val binding: DangerousPermissionRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(permissionInfo: DangerousPermissionInfo) {
			binding.apply {
				this.permissionInfo = permissionInfo
				executePendingBindings()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DangerousPermissionViewHolder {
		val binding = DangerousPermissionRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return DangerousPermissionViewHolder(binding)
	}

	override fun onBindViewHolder(holder: DangerousPermissionViewHolder, position: Int) {
		val dangerousPermission = dangerousPermissions[position]
		holder.bind(dangerousPermission)
	}

	override fun getItemCount() = dangerousPermissions.size
}