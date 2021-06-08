package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ps.omarmattr.qalby.databinding.FragmentHomeBinding
import com.ps.omarmattr.qalby.model.location.ResultLocation
import com.ps.omarmattr.qalby.model.solahTime.SendParam
import com.ps.omarmattr.qalby.model.solahTime.SolahTime
import com.ps.omarmattr.qalby.other.FunctionConstant.addSolah
import com.ps.omarmattr.qalby.ui.viewmodel.MainViewModel
import com.ps.omarmattr.qalby.ui.viewmodel.SolahViewModel
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val mBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var viewModel: SolahViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            mainViewModel.getLocationLiveData.collect {
                withContext(Dispatchers.Main) {
                    when (it.status) {
                        ResultRequest.Status.EMPTY -> {
                        }
                        ResultRequest.Status.SUCCESS -> {
                            val data = it.data as ResultLocation
                            mBinding.txtLocation.text =
                                data.address.country + " , " + data.address.city
                            viewModel.getSolahWithLatLog(
                                SendParam(
                                    latitude = data.lat.toDouble(),
                                    longitude = data.lon.toDouble(),
                                    method = 3,
                                    month = 6,
                                    year = 2021
                                )
                            )
                        }
                        ResultRequest.Status.LOADING -> {
                        }
                        ResultRequest.Status.ERROR -> {
                        }
                    }
                }

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getNextTimeLiveData.collect {
                        withContext(Dispatchers.Main) {
                            when (it.status) {
                                ResultRequest.Status.EMPTY -> {
                                    // mBinding.time.nextTime.text = "${it.data}"

                                }
                                ResultRequest.Status.SUCCESS -> {
                                    val data = it.data as String
                                    mBinding.time.text = data

                                }
                                ResultRequest.Status.LOADING -> {
                                }
                                ResultRequest.Status.ERROR -> {
                                }
                            }
                        }

                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getSolahWithLatLogLiveData.collect {
                        withContext(Dispatchers.Main) {
                            when (it.status) {
                                ResultRequest.Status.EMPTY -> {
                                }
                                ResultRequest.Status.SUCCESS -> {
                                    Log.e(
                                        this.javaClass.name,
                                        "getSolahWithLatLogLiveData SUCCESS"
                                    )

                                    val data = it.data as SolahTime
                                    val solahArray =
                                        addSolah(requireContext(), data.data.first().timings)
                                    viewModel.getNextTime(solahArray)

                                }
                                ResultRequest.Status.LOADING -> {
                                    Log.e(
                                        this.javaClass.name,
                                        "getSolahWithLatLogLiveData LOADING"
                                    )
                                }
                                ResultRequest.Status.ERROR -> {
                                    Log.e(this.javaClass.name, it.message!!)

                                }
                            }
                        }

                    }
                }

            }
        }
    }
}