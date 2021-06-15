package com.ps.omarmattr.qalby.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.ps.omarmattr.qalby.model.solahTime.Gregorian
import com.ps.omarmattr.qalby.model.solahTime.SendParam
import com.ps.omarmattr.qalby.model.solahTime.SolahItem
import com.ps.omarmattr.qalby.repository.SolahRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@HiltViewModel
class SolahViewModel @Inject constructor(
    application: Application,
    private val solahRepository: SolahRepository
) : AndroidViewModel(application) {
    private val context by lazy { application.baseContext }
    fun getSolahWithLatLog(sendParam: SendParam) = solahRepository.getPrayerTimes(sendParam)
    fun getSolahWithAddress(sendParam: SendParam, address: String) =
        solahRepository.getPrayerAddress(sendParam, address)

    fun getNextTime(solahItem: ArrayList<SolahItem>) = solahRepository.getNextTime(solahItem)
    fun alarmManager(context: Context, gregorian: Gregorian, time: String, solahItem: SolahItem) =
        solahRepository.alarmManager(context, gregorian, time, solahItem)

    val getSolahWithLatLogLiveData = solahRepository.getPrayerTimesLiveData()
    val getSolahWithAddressLiveData = solahRepository.getPrayerAddressLiveData()
    val getNextTimeLiveData = solahRepository.getCountDownTimerLiveData()

}