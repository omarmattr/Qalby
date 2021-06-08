package com.ps.omarmattr.qalby.repository

import com.ps.omarmattr.qalby.network.LocationInterface
import com.ps.omarmattr.qalby.util.ResultRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(val locationInterface: LocationInterface) {
    private val locationLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))
    fun getLocation(lat:Double,lon:Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = locationInterface.getLocation(lat = lat, lon = lon)
            try {
                if (response.isSuccessful) {
                    try {
                        response.body()?.let {
                            locationLiveData.emit(ResultRequest.success(it))
                        }
                    } catch (e: Exception) {

                    }
                } else {
                    locationLiveData.emit(
                        ResultRequest.error(
                            "Ooops: ${response.errorBody()}", ""
                        )
                    )
                }
            } catch (e: HttpException) {
                locationLiveData.emit(
                    ResultRequest.error(
                        "Ooops: ${e.message()}",
                        e
                    )
                )

            } catch (t: Throwable) {
                locationLiveData.emit(
                    ResultRequest.error(
                        "Ooops: ${t.message}",
                        t
                    )
                )
            }
        }
    }
    fun getLocationLiveData(): StateFlow<ResultRequest<Any>> = locationLiveData

}