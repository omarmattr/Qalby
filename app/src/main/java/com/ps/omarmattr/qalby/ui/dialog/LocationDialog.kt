package com.ps.omarmattr.qalby.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.ps.omarmattr.qalby.databinding.DialogLocationBinding
import com.ps.omarmattr.qalby.model.SLocation
import com.ps.omarmattr.qalby.model.solahTime.SendParam
import com.ps.omarmattr.qalby.other.PREFERENCES_ADDRESS
import com.ps.omarmattr.qalby.other.PREFERENCES_LOCATION
import com.ps.omarmattr.qalby.other.PREFERENCES_METHOD
import com.ps.omarmattr.qalby.ui.viewmodel.MainViewModel
import com.ps.omarmattr.qalby.ui.viewmodel.SolahViewModel
import com.ps.omarmattr.qalby.util.PreferencesManager
import com.ps.omarmattr.qalby.util.ResultRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class LocationDialog : BottomSheetDialogFragment() {
    private val mBinding by lazy {
        DialogLocationBinding.inflate(layoutInflater)
    }
    private lateinit var preferencesManager: PreferencesManager

    @Inject
    lateinit var viewModel: SolahViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())
        val cal = Calendar.getInstance()
        mBinding.apply {
            btnUse.setOnClickListener {
                getLocation()
                preferencesManager.editor.putBoolean(PREFERENCES_ADDRESS, false).apply()
            }
            btnSave.setOnClickListener {

                if (txtLocation.text.isNotEmpty()) {
                    viewModel.getSolahWithAddress(
                        SendParam(
                            latitude = 0.0,
                            longitude = 0.0,
                            method = preferencesManager.sharedPreferences.getInt(
                                PREFERENCES_METHOD,
                                3
                            ),
                            month = cal.get(Calendar.MONTH) + 1,
                            year = cal.get(Calendar.YEAR)
                        ), txtLocation.text.toString()
                    )
                }
                preferencesManager.editor.putBoolean(PREFERENCES_ADDRESS, true).apply()
            }
        }
    }

    fun getLocation() {
        preferencesManager.sharedPreferences.getString(
            PREFERENCES_LOCATION,
            null
        )?.let { sl ->
            val location = Gson().fromJson(sl, SLocation::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                mainViewModel.locationLiveData.emit(
                    ResultRequest.success(
                        location.lat,
                        location.lng.toString()
                    )
                )
            }
        }
    }
}