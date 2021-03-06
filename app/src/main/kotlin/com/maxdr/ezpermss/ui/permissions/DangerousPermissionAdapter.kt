package com.maxdr.ezpermss.ui.permissions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxdr.ezpermss.R
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
		return DangerousPermissionViewHolder(binding).apply {
			binding.moreOptions.setOnClickListener { buildPopupMenu(it, adapterPosition) }
		}
	}

	override fun onBindViewHolder(holder: DangerousPermissionViewHolder, position: Int) {
		val dangerousPermission = dangerousPermissions[position]
		holder.bind(dangerousPermission)
	}

	override fun getItemCount() = dangerousPermissions.size

	private fun buildPopupMenu(view: View, position: Int) {
		PopupMenu(view.context, view).apply {
			inflate(R.menu.dangerous_permission_item_context_menu)
			setOnMenuItemClickListener { item ->
				when (item.itemId) {
					R.id.more_info_summary -> {
						showFullSummary(view, position); true
					}
					R.id.set_schedule -> {
						android.util.Log.d("ITEM", position.toString()); true
					}
					else -> false
				}
			}
			show()
		}
	}

	private fun showFullSummary(view: View, position: Int) {
		val dangerousPermission = dangerousPermissions[position]
		MaterialAlertDialogBuilder(view.context).apply {
			setTitle(dangerousPermission.name)
			setMessage(dangerousPermission.summary)
			setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
		}.show()
	}
}