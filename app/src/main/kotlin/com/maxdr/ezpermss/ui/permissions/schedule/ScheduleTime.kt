package com.maxdr.ezpermss.ui.permissions.schedule

import java.time.LocalTime

sealed class ScheduleTime(val name: String, val value: LocalTime?) {
	class FiveMinutes(name: String, value: LocalTime): ScheduleTime(name, value)
	class TenMinutes(name: String, value: LocalTime): ScheduleTime(name, value)
	class FifteenMinutes(name: String, value: LocalTime): ScheduleTime(name, value)
	class ThirtyMinutes(name: String, value: LocalTime): ScheduleTime(name, value)
	class OneHour(name: String, value: LocalTime): ScheduleTime(name, value)
	class TwoHours(name: String, value: LocalTime): ScheduleTime(name, value)
	class SixHours(name: String, value: LocalTime): ScheduleTime(name, value)
	class TwelveHours(name: String, value: LocalTime): ScheduleTime(name, value)
	class OneDay(name: String, value: LocalTime): ScheduleTime(name, value)
	class CustomTime(name: String, value: LocalTime?): ScheduleTime(name, value)
}

