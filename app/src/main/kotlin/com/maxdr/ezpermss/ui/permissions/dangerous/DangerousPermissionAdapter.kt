package com.maxdr.ezpermss.ui.permissions.dangerous

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
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

		fun bind(dangerousPermissionInfo: DangerousPermissionInfo) {
			binding.apply {
				this.permissionInfo = dangerousPermissionInfo
				this.onCheckedChange = CompoundButton.OnCheckedChangeListener { _, isChecked ->
					onPermissionToggledCallback?.invoke(isChecked, bindingAdapterPosition)
				}
				executePendingBindings()
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DangerousPermissionViewHolder {
		val binding = DangerousPermissionRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return DangerousPermissionViewHolder(binding).apply {
			binding.moreOptions.setOnClickListener { buildPopupMenu(it, bindingAdapterPosition) }
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
						revokeDangerousPermission(view, position); true
					}
					else -> false
				}
			}
			show()
		}
	}

	private fun revokeDangerousPermission(view: View, position: Int) {
		MaterialAlertDialogBuilder(view.context).apply {
			setTitle(R.string.timeout_message)
			setSingleChoiceItems(R.array.timeout_names, -1) { dialog, which ->
				val timeoutValues = context.resources.getIntArray(R.array.timeout_values)
				val delayInMinutes = timeoutValues[which].toLong()
				onPermissionRevokedCallback?.invoke(position, delayInMinutes)
				dialog.dismiss()
			}
			setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
		}.show()
	}

	private fun showFullSummary(view: View, position: Int) {
		val dangerousPermission = dangerousPermissions[position]
		MaterialAlertDialogBuilder(view.context).apply {
			setTitle(dangerousPermission.name)
			setMessage(dangerousPermission.summary)
			setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
		}.show()
	}

	fun setOnPermissionToggledListener(callback: (checked: Boolean, position: Int) -> Unit) {
		onPermissionToggledCallback = callback
	}

	fun setOnPermissionRevokedListener(callback: (position: Int, delay: Long) -> Unit) {
		onPermissionRevokedCallback = callback
	}

	private var onPermissionToggledCallback: ((checked: Boolean, position: Int) -> Unit)? = null

	private var onPermissionRevokedCallback: ((position: Int, delay: Long) -> Unit)? = null
}