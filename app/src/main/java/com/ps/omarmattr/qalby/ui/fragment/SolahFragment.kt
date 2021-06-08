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
            mainViewModel.getLocationLiveData.collect {
                withContext(Dispatchers.Main) {
                    when (it.status) {
                        ResultRequest.Status.EMPTY -> {
                        }
                        ResultRequest.Status.SUCCESS -> {
                            val data = it.data as ResultLocation
                            Log.e(this@SolahFragment.javaClass.name, "getLocationLiveData")

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
                            val solahArray = addSolah(data.data.first().timings)
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

    private fun addSolah(timings: Timings): ArrayList<SolahItem> {
        return arrayListOf(
            SolahItem(
                name = getString(R.string.fajr),
                time = time24to12(timings.fajr),
                state = false
            ),
            SolahItem(
                name = getString(R.string.sunless),
                time = time24to12(timings.sunrise),
                state = false
            ), SolahItem(
                name = getString(R.string.dhuhr),
                time = time24to12(timings.dhuhr),
                state = false
            ), SolahItem(
                name = getString(R.string.asr),
                time = time24to12(timings.asr),
                state = false
            ), SolahItem(
                name = getString(R.string.maghrib),
                time = time24to12(timings.maghrib),
                state = false
            ), SolahItem(
                name = getString(R.string.sunset),
                time = time24to12(timings.sunset),
                state = false
            ), SolahItem(
                name = getString(R.string.isha),
                time = time24to12(timings.isha),
                state = false
            )
        )
    }

    private fun setUITimes(date: Date) {
        mBinding.day.text = date.gregorian.weekday.en
        mBinding.fullDate.text = date.readable
        mBinding.islamicDate.text = date.hijri.date

    }

    fun time24to12(time: String): String {
        return try {
            val sdf = SimpleDateFormat("H:mm", Locale.getDefault())
            val dateObj = sdf.parse(time)
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(dateObj!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            time
        }
    }

}