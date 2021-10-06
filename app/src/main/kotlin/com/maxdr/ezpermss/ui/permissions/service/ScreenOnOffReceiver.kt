package com.maxdr.ezpermss.ui.permissions.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenOnOffReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		if (intent.action == Intent.ACTION_SCREEN_ON) {
			android.util.Log.d("RECEIVER", "Screen ON")
		}
		else if (intent.action == Intent.ACTION_SCREEN_OFF) {
			android.util.Log.d("RECEIVER", "Screen OFF")
		}
	}
}
