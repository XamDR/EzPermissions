package com.maxdr.ezpermss.ui.permissions.dangerous

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.databinding.FragmentDangerousPermissionsBinding
import com.maxdr.ezpermss.ui.permissions.PermissionDetailFragment
import com.maxdr.ezpermss.ui.permissions.PermissionDetailViewModel
import com.maxdr.ezpermss.ui.permissions.PermissionHelper
import kotlinx.coroutines.launch

class DangerousPermissionsFragment : Fragment() {

	private var binding: FragmentDangerousPermissionsBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )
	private lateinit var bottomAdapter: DangerousPermissionAdapter
	private lateinit var topAdapter: DangerousPermissionAdapter
	private lateinit var topHeaderAdapter: HeaderDangerousPermissionAdapter
	private lateinit var bottomHeaderAdapter: HeaderDangerousPermissionAdapter

	override fun onAttach(context: Context) {
		super.onAttach(context)
		topHeaderAdapter = HeaderDangerousPermissionAdapter(getString(R.string.favorites_title))
		bottomHeaderAdapter = HeaderDangerousPermissionAdapter(getString(R.string.others_title))
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = FragmentDangerousPermissionsBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@DangerousPermissionsFragment.viewLifecycleOwner
			viewModel = this@DangerousPermissionsFragment.viewModel
		}
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		showDangerousPermissions()
	}

	private fun showDangerousPermissions() {
		viewModel.fetchDangerousPermissions(viewModel.appFullName).observe(viewLifecycleOwner) {
			topAdapter = DangerousPermissionAdapter(
				it.filter { pi -> pi.favorite }.toMutableList()
			)
			bottomAdapter = DangerousPermissionAdapter(
				it.filter { pi -> !pi.favorite }.toMutableList()
			).apply {
				val concatAdapter = if (it.isNotEmpty()) ConcatAdapter(topHeaderAdapter, topAdapter, bottomHeaderAdapter, this)
									else ConcatAdapter(this)
				binding?.recyclerView?.adapter = concatAdapter
			}
			viewModel.hasDangerousPermissions.value = it.isEmpty()
			hookListeners(it)
		}
	}

	private fun hookListeners(dangerousPermissions: List<DangerousPermissionInfo>) {
		bottomAdapter.setOnPermissionToggledListener { checked, position ->
			toggleDangerousPermissionStatus(checked, dangerousPermissions[position])
		}
		bottomAdapter.setOnPermissionRevokedListener { position, delay ->
			revokeDangerousPermissionAfterDelay(dangerousPermissions[position], delay)
		}
		bottomAdapter.setOnPermissionMovedListener { dangerousPermission ->
			topAdapter.addPermission(dangerousPermission)
			toggleDangerousPermissionFavorite(dangerousPermission, favorite = true)
		}
		topAdapter.setOnPermissionMovedListener { dangerousPermission ->
			bottomAdapter.addPermission(dangerousPermission)
			toggleDangerousPermissionFavorite(dangerousPermission, favorite = false)
		}
		topAdapter.setOnPermissionToggledListener { checked, position ->
			toggleDangerousPermissionStatus(checked, dangerousPermissions[position])
		}
	}

	private fun toggleDangerousPermissionFavorite(dangerousPermission: DangerousPermissionInfo, favorite: Boolean) {
		viewLifecycleOwner.lifecycleScope.launch {
			AppRepository.Instance.updateDangerousPermissionFavoriteInfo(
				packageName = viewModel.appFullName,
				permissionName = dangerousPermission.name,
				favorite = favorite
			)
		}
	}

	private fun toggleDangerousPermissionStatus(grant: Boolean, dangerousPermission: DangerousPermissionInfo) {
		viewLifecycleOwner.lifecycleScope.launch {
			if (grant) {
				PermissionHelper.grantDangerousPermission(
					context = requireContext(),
					packageName = viewModel.appFullName,
					permissionName = dangerousPermission.name
				)
			}
			else {
				PermissionHelper.revokeDangerousPermission(
					context = requireContext(),
					packageName = viewModel.appFullName,
					permissionName = dangerousPermission.name
				)
			}
			AppRepository.Instance.updateDangerousPermissionInfo(
				packageName = viewModel.appFullName,
				permissionName = dangerousPermission.name,
				granted = grant
			)
		}
	}

	private fun revokeDangerousPermissionAfterDelay(dangerousDangerousPermission: DangerousPermissionInfo, delay: Long) {
		val message = getString(R.string.toast_timeout_message, delay)
		Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
		(requireParentFragment() as PermissionDetailFragment).setupWorker(dangerousDangerousPermission, delay)
	}

	private fun fillAdapters() {
		viewModel.fetchDangerousPermissions(viewModel.appFullName).observe(viewLifecycleOwner) {
			viewModel.hasDangerousPermissions.value = it.isEmpty()
			topAdapter.submitList(it.filter { pi -> pi.favorite })
			bottomAdapter.submitList(it.filter { pi -> !pi.favorite })
		}
	}
}