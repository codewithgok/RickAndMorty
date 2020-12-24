package com.meksconway.rickandmorty.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.meksconway.rickandmorty.common.PageNavStatus
import com.meksconway.rickandmorty.viewmodel.RootVM

abstract class BaseFragment<VM: BaseViewModel>(view: Int) : Fragment(view) {

    abstract val viewModel: VM
    abstract val binding: ViewBinding
    abstract val pageStatus: PageNavStatus
    private val rootVM: RootVM by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            rootVM.setPageStatus(pageStatus)
        }
        viewDidLoad(savedInstanceState)
        observeViewModel(viewModel)

    }

    fun setPageStatus(status: PageNavStatus) {
        rootVM.setPageStatus(status)
    }

    open fun observeViewModel(viewModel: VM) {
        rootVM.scrollTop.observe(viewLifecycleOwner) {
            viewModel.setScrollToTop()
        }
    }

    open fun viewDidLoad(savedInstanceState: Bundle?) {

    }


}