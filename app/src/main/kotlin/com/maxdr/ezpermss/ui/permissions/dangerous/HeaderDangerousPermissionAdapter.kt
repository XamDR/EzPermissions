package com.maxdr.ezpermss.ui.permissions.dangerous

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.databinding.DangerousPermissionsHeaderBinding

class HeaderDangerousPermissionAdapter(
	private val mostUsed: Boolean) : RecyclerView.Adapter<HeaderDangerousPermissionAdapter.HeaderViewHolder>() {

	class HeaderViewHolder(
		private val binding: DangerousPermissionsHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(mostUsed: Boolean) {
			binding.apply { this.mostUsed = mostUsed }
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
		val binding = DangerousPermissionsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return HeaderViewHolder(binding)
	}

	override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
		holder.bind(mostUsed)
	}

	override fun getItemCount() = 1
}