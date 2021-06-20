package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ps.omarmattr.qalby.adapter.HomeAdapter
import com.ps.omarmattr.qalby.databinding.FragmentHomeBinding
import com.ps.omarmattr.qalby.model.home.HomeItem
import com.ps.omarmattr.qalby.model.home.social.Social
import com.ps.omarmattr.qalby.model.solahTime.SendParam
import com.ps.omarmattr.qalby.model.solahTime.SolahTime
import com.ps.omarmattr.qalby.other.PREFERENCES_ADDRESS
import com.ps.omarmattr.qalby.other.*
import com.ps.omarmattr.qalby.other.PREFERENCES_METHOD
import com.ps.omarmattr.qalby.ui.viewmodel.HomeViewModel
import com.ps.omarmattr.qalby.ui.viewmodel.MainViewModel
import com.ps.omarmattr.qalby.ui.viewmodel.SolahViewModel
import com.ps.omarmattr.qalby.util.PreferencesManager
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeAdapter.OnClickHome{
    private val mBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }



    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var viewModel: SolahViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel
    private val mAdapter by lazy {
        HomeAdapter(arrayListOf(), this)
    }
    private lateinit var preferencesManager: PreferencesManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        homeViewModel.getSocial()
        val cal = Calendar.getInstance()
        mBinding.rcHome.apply {
            adapter = mAdapter
        }

        lifecycleScope.launch {
            mainViewModel.getLocationLiveData.collect {
                withContext(Dispatchers.Main) {
                    when (it.status) {
                        ResultRequest.Status.EMPTY -> {
                        }
                        ResultRequest.Status.SUCCESS -> {
                            preferencesManager.sharedPreferences.getBoolean(
                                PREFERENCES_ADDRESS,
                                false
                            )
                                .let { b ->
                                    if (!b) {
                                        viewModel.getSolahWithLatLog(
                                            SendParam(
                                                latitude = it.data.toString().toDouble(),
                                                longitude = it.message!!.toDouble(),
                                                method = preferencesManager.sharedPreferences.getInt(
                                                    PREFERENCES_METHOD,
                                                    3
                                                ),
                                                month = cal.get(Calendar.MONTH) + 1,
                                                year = cal.get(Calendar.YEAR)
                                            )
                                        )
                                    } else {
                                        preferencesManager.sharedPreferences.getString(
                                            PREFERENCES_ADDRESS_NAME,
                                            null
                                        )?.let { name ->

                                            viewModel.getSolahWithAddress(
                                                SendParam(
                                                    latitude = it.data.toString().toDouble(),
                                                    longitude = it.message!!.toDouble(),
                                                    method = preferencesManager.sharedPreferences.getInt(
                                                        PREFERENCES_METHOD,
                                                        3
                                                    ),
                                                    month = cal.get(Calendar.MONTH) + 1,
                                                    year = cal.get(Calendar.YEAR)
                                                ), name
                                            )
                                        }
                                    }
                                }


                        }
                        ResultRequest.Status.LOADING -> {
                        }
                        ResultRequest.Status.ERROR -> {
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
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
        lifecycleScope.launch {
            homeViewModel.getSocialLiveData().collect {
                withContext(Dispatchers.Main) {
                    when (it.status) {
                        ResultRequest.Status.EMPTY -> {
                            // mBinding.time.nextTime.text = "${it.data}"

                        }
                        ResultRequest.Status.SUCCESS -> {
                            val data = it.data as Social
                            if (mAdapter.arrayList.size < 2) mAdapter.arrayList.add(
                                HomeItem(
                                    name = "Instagram",
                                    social = data
                                )
                            )
                            mAdapter.notifyDataSetChanged()
                            mAdapter.notifyDataSetChanged()

                        }
                        ResultRequest.Status.LOADING -> {
                        }
                        ResultRequest.Status.ERROR -> {
                        }
                    }
                }

            }
        }
        lifecycleScope.launch {
            homeViewModel.getSocialFacebookLiveData().collect {
                withContext(Dispatchers.Main) {
                    when (it.status) {
                        ResultRequest.Status.EMPTY -> {

                        }
                        ResultRequest.Status.SUCCESS -> {
                            val dataFace = it.data as Social
                            Log.e(
                                this.javaClass.name,
                                "getSocialFacebookLiveData SUCCESS"
                            )
                            if (mAdapter.arrayList.size < 2) mAdapter.arrayList.add(
                                HomeItem(
                                    name = "Facebook",
                                    social = dataFace
                                )
                            )
                            mAdapter.notifyDataSetChanged()

                        }
                        ResultRequest.Status.LOADING -> {
                            Log.e(
                                this.javaClass.name,
                                "getSocialFacebookLiveData LOADING"
                            )
                        }
                        ResultRequest.Status.ERROR -> {
                            Log.e(
                                this.javaClass.name,
                                "getSocialFacebookLiveData ${it.message}"
                            )
                        }
                    }
                }

            }
        }
        lifecycleScope.launch {
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
                            data.data.find {
                                it.date.gregorian.day.toInt() == cal.get(Calendar.DAY_OF_MONTH)
                            }?.let {
                                mBinding.txtLocation.text = it.meta.timezone
                                viewModel.getNextTime(
                                    FunctionConstant.addSolah(
                                        requireContext(),
                                        it.timings
                                    )
                                )

                            }


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
        lifecycleScope.launch {
            viewModel.getSolahWithAddressLiveData.collect {
                withContext(Dispatchers.Main) {
                    when (it.status) {
                        ResultRequest.Status.EMPTY -> {
                        }
                        ResultRequest.Status.SUCCESS -> {
                            Log.e(
                                this.javaClass.name,
                                "getSolahWithAddressLiveData SUCCESS"
                            )

                            val data = it.data as SolahTime
                            data.data.find {
                                it.date.gregorian.day.toInt() == cal.get(Calendar.DAY_OF_MONTH)
                            }?.let {
                                mBinding.txtLocation.text = it.meta.timezone
                                viewModel.getNextTime(
                                    FunctionConstant.addSolah(
                                        requireContext(),
                                        it.timings
                                    )
                                )

                            }


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



    override fun onClick(itemHome: HomeItem) {
    }
}