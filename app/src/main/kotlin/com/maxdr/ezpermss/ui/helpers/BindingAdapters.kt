package com.maxdr.ezpermss.ui.helpers

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.maxdr.ezpermss.util.debug

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter(value = ["packageName", "drawableResId"])
fun setImageDrawable(imageView: ImageView, packageName: String, drawableResId: Int) {
	try {
		val resources = imageView.context.packageManager.getResourcesForApplication(packageName)
		val drawable = resources.getDrawable(drawableResId, null)
		imageView.setImageDrawable(drawable)
	}
	catch (e: PackageManager.NameNotFoundException) {
		debug("PM", e.message)
	}
}