package com.maxdr.ezpermss.ui.permissions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.NonDangerousPermissionInfo
import com.maxdr.ezpermss.databinding.NondangerousPermissionRowLayoutBinding

class NonDangerousPermissionAdapter(private val permissions: List<NonDangerousPermissionInfo>) :
	RecyclerView.Adapter<NonDangerousPermissionAdapter.NonDangerousPermissionViewHolder>() {

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