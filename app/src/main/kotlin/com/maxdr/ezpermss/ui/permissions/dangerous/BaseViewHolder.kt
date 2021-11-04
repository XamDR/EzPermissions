package com.maxdr.ezpermss.ui.permissions.dangerous

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.maxdr.ezpermss.core.DangerousPermissionInfo

abstract class BaseViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

	abstract fun bind(dangerousPermissionInfo: DangerousPermissionInfo)
}