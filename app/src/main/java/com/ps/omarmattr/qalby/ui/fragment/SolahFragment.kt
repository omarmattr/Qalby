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
import com.ps.omarmattr.qalby.model.solahTime.Data
import com.ps.omarmattr.qalby.model.solahTime.Date
import com.ps.omarmattr.qalby.model.solahTime.SolahItem
import com.ps.omarmattr.qalby.model.solahTime.SolahTime
import com.ps.omarmattr.qalby.other.AZAN_KEY
import com.ps.omarmattr.qalby.other.FunctionConstant.addSolah
import com.ps.omarmattr.qalby.other.PREFERENCES_IS_ALARM
import com.ps.omarmattr.qalby.ui.dialog.AzanDialog
import com.ps.omarmattr.qalby.ui.viewmodel.MainViewModel
import com.ps.omarmattr.qalby.ui.viewmodel.SolahViewModel
import com.ps.omarmattr.qalby.util.PreferencesManager
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_solah.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())
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
                            val cal1 = Calendar.getInstance()
                            val data = it.data as SolahTime
                            preferencesManager.sharedPreferences.getBoolean(
                                PREFERENCES_IS_ALARM,
                                false
                            ).let {
                                if (!it)
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val after = data.data.filter { i ->
                                            val cal2 = Calendar.getInstance()
                                            cal2.clear()
                                            cal2.set(Calendar.YEAR, i.date.gregorian.year.toInt())
                                            cal2.set(
                                                Calendar.MONTH,
                                                i.date.gregorian.month.number - 1
                                            )
                                            cal2.set(
                                                Calendar.DAY_OF_MONTH,
                                                i.date.gregorian.day.toInt()
                                            )
                                            cal1.get(Calendar.DAY_OF_YEAR) <= cal2.get(Calendar.DAY_OF_YEAR) &&
                                                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)

                                        }


                                        Log.e("ppppppp2", after.toString())
                                        after.forEach { i ->
                                            addAlarm(i, i.timings.fajr, getString(R.string.fajr))
                                            addAlarm(
                                                i,
                                                i.timings.sunrise,
                                                getString(R.string.sunless)
                                            )
                                            addAlarm(i, i.timings.dhuhr, getString(R.string.dhuhr))
                                            addAlarm(i, i.timings.asr, getString(R.string.asr))
                                            addAlarm(
                                                i,
                                                i.timings.maghrib,
                                                getString(R.string.maghrib)
                                            )
                                            addAlarm(
                                                i,
                                                i.timings.sunset,
                                                getString(R.string.sunset)
                                            )
                                            addAlarm(i, i.timings.isha, getString(R.string.isha))

                                        }
                                        withContext(Dispatchers.Main) {
                                            preferencesManager.editor.putBoolean(
                                                PREFERENCES_IS_ALARM,
                                                true
                                            ).apply()
                                        }
                                    }

                            }

                            data.data.find {
                                it.date.gregorian.day.toInt() == cal1.get(Calendar.DAY_OF_MONTH)
                            }?.let {
                                setUITimes(it.date)
                                val solahArray = addSolah(requireContext(), it.timings)
                                mAdapter.data = solahArray
                                viewModel.getNextTime(solahArray)
                            }


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
        val bundle = Bundle()
        bundle.putParcelable(AZAN_KEY, itemViewModel)
        val azanDialog = AzanDialog()
        azanDialog.arguments = bundle
        azanDialog.show(childFragmentManager, "")
    }

    private fun setUITimes(date: Date) {
        mBinding.day.text = date.gregorian.weekday.en
        mBinding.fullDate.text = date.readable
        mBinding.islamicDate.text = date.hijri.date

    }

    fun addAlarm(data: Data, time: String, name: String) {
        viewModel.alarmManager(
            requireContext(), data.date.gregorian, time,
            SolahItem(
                name = name,
                time = time,
                state = false
            )
        )
    }


}