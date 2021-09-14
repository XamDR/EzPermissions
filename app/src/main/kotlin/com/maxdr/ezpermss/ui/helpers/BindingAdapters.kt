package com.maxdr.ezpermss.ui.helpers

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("src")
fun setImageDrawable(imageView: ImageView, drawable: Drawable) {
	imageView.setImageDrawable(drawable)
}