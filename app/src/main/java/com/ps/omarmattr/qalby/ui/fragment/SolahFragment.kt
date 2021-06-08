package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.adapter.GenericAdapter
import com.ps.omarmattr.qalby.databinding.FragmentSolahBinding
import com.ps.omarmattr.qalby.model.location.ResultLocation
import com.ps.omarmattr.qalby.model.solahTime.*
import com.ps.omarmattr.qalby.model.solahTime.Date
import com.ps.omarmattr.qalby.other.FunctionConstant.addSolah
import com.ps.omarmattr.qalby.ui.viewmodel.MainViewModel
import com.ps.omarmattr.qalby.ui.viewmodel.SolahViewModel
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_solah.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SolahFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<SolahItem> {

    @Inject
    lateinit var viewModel: SolahViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel
    private val mBinding by lazy {
        FragmentSolahBinding.inflate(layoutInflater)
    }
    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_solah, BR.solah, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {
            goToSetting.setOnClickListener {
                findNavController().navigate(R.id.action_destination_solah_to_settingSolahFragment)
            }
            rcSolah.apply {
                adapter = mAdapter
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
                                this@SolahFragment.javaClass.name,
                                "getSolahWithLatLogLiveData SUCCESS"
                            )

                            val data = it.data as SolahTime

                            setUITimes(data.data.first().date)
                            val solahArray = addSolah(requireContext(),data.data.first().timings)
                            mAdapter.data = solahArray
                            viewModel.getNextTime(solahArray)

                        }
                        ResultRequest.Status.LOADING -> {
                            Log.e(
                                this@SolahFragment.javaClass.name,
                                "getSolahWithLatLogLiveData LOADING"
                            )
                        }
                        ResultRequest.Status.ERROR -> {
                            Log.e(this@SolahFragment.javaClass.name, it.message!!)

                        }
                    }
                }

            }
        }
    }

    override fun onClickItem(itemViewModel: SolahItem, type: Int, position: Int) {

    }

    private fun setUITimes(date: Date) {
        mBinding.day.text = date.gregorian.weekday.en
        mBinding.fullDate.text = date.readable
        mBinding.islamicDate.text = date.hijri.date

    }

}