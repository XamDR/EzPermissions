package com.maxdr.ezpermss.ui.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.maxdr.ezpermss.databinding.FragmentAppListBinding

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
			binding?.recyclerView?.adapter = AppInfoAdapter(it)
		}
		binding?.recyclerView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
	}
}