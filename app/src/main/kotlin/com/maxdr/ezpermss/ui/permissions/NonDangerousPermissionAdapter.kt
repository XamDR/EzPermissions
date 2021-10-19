package com.maxdr.ezpermss.ui.permissions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.PermissionInfo
import com.maxdr.ezpermss.databinding.NondangerousPermissionRowLayoutBinding

class NonDangerousPermissionAdapter(private val permissions: List<PermissionInfo>) :
	RecyclerView.Adapter<NonDangerousPermissionAdapter.NonDangerousPermissionViewHolder>() {

	inner class NonDangerousPermissionViewHolder(
		private val binding: NondangerousPermissionRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(permissionInfo: PermissionInfo) {
			binding.apply {
				this.permissionInfo = permissionInfo
				executePendingBindings()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NonDangerousPermissionViewHolder {
		val binding = NondangerousPermissionRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return NonDangerousPermissionViewHolder(binding).apply {
			binding.moreInfo.setOnClickListener { showFullSummary(it, adapterPosition) }
		}
	}

	override fun onBindViewHolder(holder: NonDangerousPermissionViewHolder, position: Int) {
		val normalPermission = permissions[position]
		holder.bind(normalPermission)
	}

	override fun getItemCount() = permissions.size

	private fun showFullSummary(view: View, position: Int) {
		val nonDangerousPermission = permissions[position]
		MaterialAlertDialogBuilder(view.context).apply {
			setTitle(nonDangerousPermission.name)
			setMessage(nonDangerousPermission.summary)
			setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
		}.show()
	}
}