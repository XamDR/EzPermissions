package com.maxdr.ezpermss.ui.permissions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.NormalPermissionInfo
import com.maxdr.ezpermss.databinding.NormalPermissionRowLayoutBinding

class NormalPermissionAdapter(private val normalPermissions: List<NormalPermissionInfo>) :
	RecyclerView.Adapter<NormalPermissionAdapter.NonDangerousPermissionViewHolder>() {

	inner class NonDangerousPermissionViewHolder(
		private val binding: NormalPermissionRowLayoutBinding
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(permissionInfo: NormalPermissionInfo) {
			binding.apply {
				this.permissionInfo = permissionInfo
				executePendingBindings()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NonDangerousPermissionViewHolder {
		val binding = NormalPermissionRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return NonDangerousPermissionViewHolder(binding).apply {
			binding.moreInfo.setOnClickListener { showFullSummary(it, adapterPosition) }
		}
	}

	override fun onBindViewHolder(holder: NonDangerousPermissionViewHolder, position: Int) {
		val normalPermission = normalPermissions[position]
		holder.bind(normalPermission)
	}

	override fun getItemCount() = normalPermissions.size

	private fun showFullSummary(view: View, position: Int) {
		val nonDangerousPermission = normalPermissions[position]
		MaterialAlertDialogBuilder(view.context).apply {
			setTitle(nonDangerousPermission.name)
			setMessage(nonDangerousPermission.summary)
			setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
		}.show()
	}
}