package com.maxdr.ezpermss.ui.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

object ImageStorageManager {

	fun saveToInternalStorage(context: Context, bitmapImage: Bitmap?, imageFileName: String): String {
		context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use { fos ->
			bitmapImage?.compress(Bitmap.CompressFormat.PNG, 25, fos)
		}
		return context.filesDir.absolutePath
	}

	fun getImageFromInternalStorage(context: Context, imageFileName: String): Bitmap? {
		val directory = context.filesDir
		val file = File(directory, imageFileName)
		return try {
			BitmapFactory.decodeStream(FileInputStream(file))
		}
		catch (e: FileNotFoundException) {
			null
		}
	}
}