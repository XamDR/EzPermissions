package com.maxdr.ezpermss.ui.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.maxdr.ezpermss.databinding.AppListFragmentBinding
import com.maxdr.ezpermss.ui.permissions.service.ForegroundServiceHandler
import com.maxdr.ezpermss.util.mainActivity

class AppListFragment : Fragment() {

	private var binding: AppListFragmentBinding? = null
	private val viewModel by viewModels<AppListViewModel> {
		ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = AppListFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@AppListFragment.viewLifecycleOwner
			handler = ForegroundServiceHandler(requireContext())
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
			binding?.recyclerView?.adapter = AppInfoAdapter(it, mainActivity)
		}
		binding?.recyclerView?.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
	}
}