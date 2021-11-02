package com.maxdr.ezpermss.ui.permissions.dangerous

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.databinding.DangerousPermissionsHeaderBinding

class HeaderDangerousPermissionAdapter(private val title: String) : RecyclerView.Adapter<HeaderDangerousPermissionAdapter.HeaderViewHolder>() {

	class HeaderViewHolder(private val binding: DangerousPermissionsHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(title: String) {
			binding.headerTitle.text = title
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
		val binding = DangerousPermissionsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return HeaderViewHolder(binding)
	}

	override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
		holder.bind(title)
	}

	override fun getItemCount() = 1
}