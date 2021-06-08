package com.ps.omarmattr.qalby.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ps.omarmattr.qalby.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val locationRepository: LocationRepository
) : AndroidViewModel(application) {

    fun getLocation(lat: Double, lon: Double) = locationRepository.getLocation(lat, lon)
    val getLocationLiveData = locationRepository.getLocationLiveData()

}