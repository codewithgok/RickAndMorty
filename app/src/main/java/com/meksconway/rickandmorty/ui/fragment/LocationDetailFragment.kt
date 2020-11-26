package com.meksconway.rickandmorty.ui.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.meksconway.rickandmorty.R
import com.meksconway.rickandmorty.base.BaseFragment
import com.meksconway.rickandmorty.common.PageNavStatus
import com.meksconway.rickandmorty.common.RMNavigator
import com.meksconway.rickandmorty.common.Status.*
import com.meksconway.rickandmorty.common.viewBinding
import com.meksconway.rickandmorty.databinding.FragmentLocationDetailBinding
import com.meksconway.rickandmorty.ui.adapter.locationdetail.LocationDetailAdapter
import com.meksconway.rickandmorty.viewmodel.LocationDetailVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LocationDetailFragment : BaseFragment<LocationDetailVM>
    (R.layout.fragment_location_detail) {

    override val viewModel: LocationDetailVM by viewModels()
    override val binding by viewBinding(FragmentLocationDetailBinding::bind)
    override val pageStatus = PageNavStatus.childFragmentStatus(null)

    private val locationDetailAdapter = LocationDetailAdapter()


    override fun viewDidLoad(savedInstanceState: Bundle?) {
        super.viewDidLoad(savedInstanceState)
        lifecycleScope.launchWhenResumed {

            val locationId = arguments?.getString(LOCATION_ID)
            val locationName = arguments?.getString(LOCATION_NAME)
            setPageStatus(PageNavStatus.childFragmentStatus(locationName))
            val layoutManager = LinearLayoutManager(context)
            binding.rvLocationDetail.setItemViewCacheSize(8)
            binding.rvLocationDetail.adapter = locationDetailAdapter
            binding.rvLocationDetail.layoutManager = layoutManager

            viewModel.getLocation(locationId)
        }
    }

    override fun observeViewModel(viewModel: LocationDetailVM) {
        super.observeViewModel(viewModel)
        viewModel.locationData.observe(viewLifecycleOwner) {
            lifecycleScope.launchWhenResumed {
                when (it.status) {
                    SUCCESS -> {
                        locationDetailAdapter.submitList(it.data)
                        binding.progressBar.isGone = true

                    }
                    FAIL -> {
                        Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                        delay(400)
                        RMNavigator.popBack()
                    }
                    LOADING -> {
                        binding.progressBar.isVisible = true
                    }
                }
            }
        }
    }

    companion object {
        private const val LOCATION_ID = "location_id"
        private const val LOCATION_NAME = "location_name"

        fun newInstance(locId: String?, locName: String?) = LocationDetailFragment().apply {
            Bundle().apply {
                arguments = Bundle().apply {
                    putString(LOCATION_ID, locId)
                    putString(LOCATION_NAME, locName)
                }
            }
        }
    }


}