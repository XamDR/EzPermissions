package com.maxdr.ezpermss.ui.permissions.schedule

import android.content.Context
import com.maxdr.ezpermss.R
import java.time.LocalTime

class TimePickerHelper(context: Context) {

	val times = mutableListOf(
		ScheduleTime.FiveMinutes(name = context.getString(R.string.five_minutes), value = addFiveMinutes()),
		ScheduleTime.TenMinutes(name = context.getString(R.string.ten_minutes), value = addTenMinutes()),
		ScheduleTime.FifteenMinutes(name = context.getString(R.string.fifteen_minutes), value = addFifteenMinutes()),
		ScheduleTime.ThirtyMinutes(name = context.getString(R.string.thirty_minutes), value = addThirtyMinutes()),
		ScheduleTime.OneHour(name = context.getString(R.string.one_hour), value = addOneHour()),
		ScheduleTime.TwoHours(name = context.getString(R.string.two_hours), value = addTwoHours()),
		ScheduleTime.SixHours(name = context.getString(R.string.six_hours), value = addSixHours()),
		ScheduleTime.TwelveHours(name = context.getString(R.string.twelve_hours), value = addTwelveHours()),
		ScheduleTime.OneDay(name = context.getString(R.string.one_day), value = addOneDay()),
		ScheduleTime.CustomTime(name = context.getString(R.string.custom_time), value = null)
	)

	private fun addFiveMinutes() = LocalTime.now().plusMinutes(5)

	private fun addTenMinutes() = LocalTime.now().plusMinutes(10)

	private fun addFifteenMinutes() = LocalTime.now().plusMinutes(15)

	private fun addThirtyMinutes() = LocalTime.now().plusMinutes(30)

	private fun addOneHour() = LocalTime.now().plusHours(1)

	private fun addTwoHours() = LocalTime.now().plusHours(2)

	private fun addSixHours() = LocalTime.now().plusHours(6)

	private fun addTwelveHours() = LocalTime.now().plusHours(12)

	private fun addOneDay() = LocalTime.now().plusHours(24)
}