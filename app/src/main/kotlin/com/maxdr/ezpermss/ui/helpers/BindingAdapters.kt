package com.maxdr.ezpermss.ui.helpers

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("src")
fun setImageDrawable(imageView: ImageView, bitmapPath: String) {
	val bitmap = ImageStorageManager.getImageFromInternalStorage(imageView.context, bitmapPath)
	imageView.setImageBitmap(bitmap)
}