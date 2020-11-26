package com.meksconway.rickandmorty.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.meksconway.rickandmorty.common.PageNavStatus
import com.meksconway.rickandmorty.viewmodel.RootVM
import com.trendyol.medusalib.navigator.MultipleStackNavigator

abstract class BaseFragment<VM: BaseViewModel>(view: Int) : Fragment(view) {

//    var multipleStackNavigator: MultipleStackNavigator? = null
    abstract val viewModel: VM
    abstract val binding: ViewBinding
    abstract val pageStatus: PageNavStatus
    private val rootVM: RootVM by activityViewModels()


    private fun initStackNavigator(context: Context?) {
//        if (context is RootActivity && multipleStackNavigator == null) {
//             multipleStackNavigator = context.navigator
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        initStackNavigator(context)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initStackNavigator(context)
    }


}