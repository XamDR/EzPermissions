package com.maxdr.ezpermss.ui.permissions.schedule

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.databinding.TimePickerFragmentBinding
import com.maxdr.ezpermss.ui.permissions.PermissionDetailFragment
import com.maxdr.ezpermss.util.debug
import com.maxdr.ezpermss.views.DropDownView
import java.time.Duration
import java.time.LocalTime

class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener{

	private var binding: TimePickerFragmentBinding? = null
	private lateinit var helper: TimePickerHelper
	private lateinit var timeAdapter: ScheduleTimeAdapter
	private lateinit var selectedTime: LocalTime
	private var dangerousPermissionInfo: DangerousPermissionInfo? = null
	private val now = LocalTime.now()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		helper = TimePickerHelper(requireContext())
		selectedTime = helper.times[0].value!!
		timeAdapter = ScheduleTimeAdapter(requireContext(), R.layout.spinner_item, helper.times)
		dangerousPermissionInfo = arguments?.getParcelable("permission")
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		binding = TimePickerFragmentBinding.inflate(LayoutInflater.from(context)).apply {
			spinnerTime.adapter = timeAdapter
			(spinnerTime.adapter as ScheduleTimeAdapter).setOnItemClickListener { _, _, position, _ ->
				onItemClick(spinnerTime, position)
			}
		}
		return MaterialAlertDialogBuilder(requireContext())
			.setTitle(dangerousPermissionInfo?.name)
			.setView(binding?.root)
			.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
			.setPositiveButton(R.string.ok) { dialog, _ ->
				dialog.dismiss()
				scheduleRevokeDangerousPermission()
			}.show()
	}

	private fun scheduleRevokeDangerousPermission() {
		dangerousPermissionInfo?.let {
			val delay = Duration.between(now, selectedTime).toMinutes()
			val message = getString(R.string.timeout_message, delay)
			Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
			(requireParentFragment() as PermissionDetailFragment).setupWorker(it, delay)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
		selectedTime = LocalTime.of(hourOfDay, minute)
		helper.times[helper.times.size - 1] = ScheduleTime.CustomTime(getString(R.string.custom_time), value = selectedTime)
		timeAdapter.notifyDataSetChanged()
		binding?.spinnerTime?.setSelection(helper.times.size - 1)
	}

	private fun onItemClick(dropDownView: DropDownView, position: Int) {
		dropDownView.hideDropDown()

		if (position == helper.times.size - 1) {
			showTimePickerDialog()
		}
		else {
			dropDownView.setSelection(position)
			selectedTime = helper.times[position].value!!
			debug("TIME_SELECTED", selectedTime)
		}
	}

	private fun showTimePickerDialog() {
		if (::selectedTime.isInitialized) {
			TimePickerDialog(
				requireContext(),
				this,
				selectedTime.hour,
				selectedTime.minute,
				DateFormat.is24HourFormat(context)
			).show()
		}
	}

	companion object {
		fun newInstance(value: DangerousPermissionInfo): TimePickerDialogFragment {
			val fragment = TimePickerDialogFragment()
			val bundle = Bundle().apply {
				putParcelable("permission", value)
			}
			fragment.arguments = bundle
			return fragment
		}
	}
}