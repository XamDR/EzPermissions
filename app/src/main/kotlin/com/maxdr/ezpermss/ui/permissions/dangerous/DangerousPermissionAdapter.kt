package com.maxdr.ezpermss.ui.permissions.dangerous

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.PopupMenu
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.databinding.DangerousPermissionRowLayoutBinding
import com.topjohnwu.superuser.Shell

class DangerousPermissionAdapter :
	ListAdapter<DangerousPermissionInfo, DangerousPermissionAdapter.DangerousPermissionViewHolder>(DangerousPermissionsCallback()) {

	inner class DangerousPermissionViewHolder(
		private val binding: DangerousPermissionRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

		fun bind(dangerousPermissionInfo: DangerousPermissionInfo) {
			binding.apply {
				this.permissionInfo = dangerousPermissionInfo
				this.onCheckedChange = CompoundButton.OnCheckedChangeListener { view, isChecked ->
					if (view.isShown) {
						if (Shell.rootAccess()) {
							onPermissionToggledCallback?.invoke(isChecked, getItem(bindingAdapterPosition))
						}
						else {
							togglePermission.isChecked = !togglePermission.isChecked
							showNoRootAlertDialog(root.context)
						}
					}
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
		val dangerousPermission = getItem(position)
		holder.bind(dangerousPermission)
	}

	fun setOnPermissionToggledListener(callback: (checked: Boolean, dangerousPermission: DangerousPermissionInfo) -> Unit) {
		onPermissionToggledCallback = callback
	}

	fun setOnPermissionRevokedListener(callback: (dangerousPermission: DangerousPermissionInfo, delay: Long) -> Unit) {
		onPermissionRevokedCallback = callback
	}

	fun setOnPermissionMovedListener(callback: (dangerousPermission: DangerousPermissionInfo) -> Unit) {
		onPermissionMovedCallback = callback
	}

	private fun showNoRootAlertDialog(context: Context) {
		val formattedString = HtmlCompat.fromHtml(
			context.getText(R.string.no_root_msg).toString(), HtmlCompat.FROM_HTML_MODE_COMPACT
		)
		MaterialAlertDialogBuilder(context).apply {
			setTitle(R.string.app_name)
			setMessage(formattedString)
			setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
		}.show()
	}

	private fun buildPopupMenu(view: View, position: Int) {
		val dangerousPermission = getItem(position)
		PopupMenu(view.context, view).apply {
			if (!dangerousPermission.favorite) {
				inflate(R.menu.favorite_dangerous_permission_context_menu)
				setOnMenuItemClickListener { item ->
					when (item.itemId) {
						R.id.more_info_summary -> {
							showFullSummary(view, position); true
						}
						R.id.set_timeout -> {
							revokeDangerousPermission(view, position); true
						}
						R.id.add_favorites -> {
							movePermission(position); true
						}
						else -> false
					}
				}
			}
			else {
				inflate(R.menu.non_favorite_dangerous_permission_context_menu)
				setOnMenuItemClickListener { item ->
					when (item.itemId) {
						R.id.more_info_summary -> {
							showFullSummary(view, position); true
						}
						R.id.add_others -> {
							movePermission(position); true
						}
						else -> false
					}
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
				onPermissionRevokedCallback?.invoke(getItem(position), delayInMinutes)
				dialog.dismiss()
			}
			setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
		}.show()
	}

	private fun showFullSummary(view: View, position: Int) {
		val dangerousPermission = getItem(position)
		MaterialAlertDialogBuilder(view.context).apply {
			setTitle(dangerousPermission.name)
			setMessage(dangerousPermission.summary)
			setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
		}.show()
	}

	private fun movePermission(position: Int) {
		val dangerousPermission = getItem(position)
		onPermissionMovedCallback?.invoke(dangerousPermission)
	}

	private var onPermissionToggledCallback: ((checked: Boolean, dangerousPermission: DangerousPermissionInfo) -> Unit)? = null

	private var onPermissionRevokedCallback: ((dangerousPermission: DangerousPermissionInfo, delay: Long) -> Unit)? = null

	private var onPermissionMovedCallback: ((dangerousPermission: DangerousPermissionInfo) -> Unit?)? = null

	private class DangerousPermissionsCallback : DiffUtil.ItemCallback<DangerousPermissionInfo>() {

		override fun areItemsTheSame(oldPermissionInfo: DangerousPermissionInfo, newPermissionInfo: DangerousPermissionInfo)
			= oldPermissionInfo.name == newPermissionInfo.name

		override fun areContentsTheSame(oldPermissionInfo: DangerousPermissionInfo, newPermissionInfo: DangerousPermissionInfo)
			= oldPermissionInfo == newPermissionInfo
	}
}