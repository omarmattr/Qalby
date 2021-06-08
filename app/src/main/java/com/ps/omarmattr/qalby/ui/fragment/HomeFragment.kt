package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ps.omarmattr.qalby.databinding.FragmentHomeBinding
import com.ps.omarmattr.qalby.model.location.ResultLocation
import com.ps.omarmattr.qalby.ui.viewmodel.MainViewModel
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment:Fragment() {
    private val mBinding by lazy{
        FragmentHomeBinding.inflate(layoutInflater)
    }
    @Inject
    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
        viewModel.getLocationLiveData.collect {
            withContext(Dispatchers.Main) {
            when(it.status){
                ResultRequest.Status.EMPTY->{}
                ResultRequest.Status.SUCCESS->{
                    val data = it.data as ResultLocation
                    mBinding.txtLocation.text = data.address.country +" , " +data.address.city

                }
                ResultRequest.Status.LOADING->{}
                ResultRequest.Status.ERROR->{}
            }
            }

        }
        }
    }
}