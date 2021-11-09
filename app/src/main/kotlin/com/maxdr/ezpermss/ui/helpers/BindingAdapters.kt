package com.maxdr.ezpermss.ui.helpers

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter(value = ["packageName", "drawableResId"])
fun setImageDrawable(imageView: ImageView, packageName: String, drawableResId: Int) {
	val resources = imageView.context.packageManager.getResourcesForApplication(packageName)
	val drawable = resources.getDrawable(drawableResId, null)
	imageView.setImageDrawable(drawable)
}