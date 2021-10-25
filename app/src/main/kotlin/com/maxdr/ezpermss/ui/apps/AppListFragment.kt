package com.maxdr.ezpermss.ui.apps

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.databinding.FragmentAppListBinding
import com.maxdr.ezpermss.ui.helpers.PreferencesManager
import com.maxdr.ezpermss.ui.permissions.service.PermissionService
import com.maxdr.ezpermss.util.mainActivity

class AppListFragment : Fragment() {

	private var binding: FragmentAppListBinding? = null
	private val viewModel by viewModels<AppListViewModel>()

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = FragmentAppListBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@AppListFragment.viewLifecycleOwner
			viewModel = this@AppListFragment.viewModel
		}
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.appInfoLiveData.observe(viewLifecycleOwner) {
			binding?.recyclerView?.adapter = AppInfoAdapter(it, mainActivity).apply {
				stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
			}
		}
		binding?.recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
		setupFab()
	}

	private fun setupFab() {
		val manager = PreferencesManager(requireContext())
		val resId = if (manager.isServiceRunning) R.drawable.ic_stop_service else R.drawable.ic_start_service
		binding?.fabService?.apply {
			setImageResource(resId)
			setOnClickListener { togglePermissionService(it) }
		}
	}

	private fun togglePermissionService(fab: View) {
		val manager = PreferencesManager(requireContext())
		val permissionIntent = Intent(context, PermissionService::class.java)
		val resId: Int

		if (!manager.isServiceRunning) {
			permissionIntent.action = PermissionService.SERVICE_START_ACTION
			manager.isServiceRunning = true
			resId = R.drawable.ic_stop_service
		}
		else {
			permissionIntent.action = PermissionService.SERVICE_STOP_ACTION
			manager.isServiceRunning = false
			resId = R.drawable.ic_start_service
		}
		(fab as FloatingActionButton).setImageResource(resId)
		ContextCompat.startForegroundService(requireContext(), permissionIntent)
	}
}