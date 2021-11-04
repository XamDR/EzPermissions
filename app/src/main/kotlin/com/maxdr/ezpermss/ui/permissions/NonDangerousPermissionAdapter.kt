package com.maxdr.ezpermss.ui.permissions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.NonDangerousPermissionInfo
import com.maxdr.ezpermss.databinding.NondangerousPermissionRowLayoutBinding

class NonDangerousPermissionAdapter : ListAdapter<
		NonDangerousPermissionInfo, NonDangerousPermissionAdapter.NonDangerousPermissionViewHolder
		>(NonDangerousPermissionsCallback()) {

	inner class NonDangerousPermissionViewHolder(
		private val binding: NondangerousPermissionRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(nonDangerousPermissionInfo: NonDangerousPermissionInfo) {
			binding.apply {
				this.permissionInfo = nonDangerousPermissionInfo
				executePendingBindings()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NonDangerousPermissionViewHolder {
		val binding = NondangerousPermissionRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return NonDangerousPermissionViewHolder(binding).apply {
			binding.moreInfo.setOnClickListener { showFullSummary(it, bindingAdapterPosition) }
		}
	}

	override fun onBindViewHolder(holder: NonDangerousPermissionViewHolder, position: Int) {
		val normalPermission = getItem(position)
		holder.bind(normalPermission)
	}

	private fun showFullSummary(view: View, position: Int) {
		val nonDangerousPermission = getItem(position)
		MaterialAlertDialogBuilder(view.context).apply {
			setTitle(nonDangerousPermission.name)
			setMessage(nonDangerousPermission.summary)
			setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
		}.show()
	}

	private class NonDangerousPermissionsCallback : DiffUtil.ItemCallback<NonDangerousPermissionInfo>() {

		override fun areItemsTheSame(oldPermissionInfo: NonDangerousPermissionInfo, newPermissionInfo: NonDangerousPermissionInfo)
			= oldPermissionInfo.name == newPermissionInfo.name

		override fun areContentsTheSame(oldPermissionInfo: NonDangerousPermissionInfo, newPermissionInfo: NonDangerousPermissionInfo)
			= oldPermissionInfo == newPermissionInfo
	}
}