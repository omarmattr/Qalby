package com.ps.omarmattr.qalby.repository

import com.ps.omarmattr.qalby.network.LocationInterface
import com.ps.omarmattr.qalby.network.SocialInterface
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
class SocialRepository @Inject constructor(val socialInterface: SocialInterface) {
    private val socialLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))
    private val socialFacebookLiveData: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))

    fun getSocial() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = socialInterface.getFacebook()
            try {
                if (response.isSuccessful) {
                    try {
                        response.body()?.let {
                            socialLiveData.emit(ResultRequest.success(it))
                        }
                    } catch (e: Exception) {

                    }
                } else {
                    socialLiveData.emit(
                        ResultRequest.error(
                            "Ooops: ${response.errorBody()}", ""
                        )
                    )
                }
            } catch (e: HttpException) {
                socialLiveData.emit(
                    ResultRequest.error(
                        "Ooops: ${e.message()}",
                        e
                    )
                )

            } catch (t: Throwable) {
                socialLiveData.emit(
                    ResultRequest.error(
                        "Ooops: ${t.message}",
                        t
                    )
                )
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            val response = socialInterface.getInstagram()
            try {
                if (response.isSuccessful) {
                    try {
                        response.body()?.let {
                            socialFacebookLiveData.emit(ResultRequest.success(it))
                        }
                    } catch (e: Exception) {

                    }
                } else {
                    socialFacebookLiveData.emit(
                        ResultRequest.error(
                            "Ooops1 ${response.errorBody()!!.charStream().readText()}", ""
                        )
                    )
                }
            } catch (e: HttpException) {
                socialFacebookLiveData.emit(
                    ResultRequest.error(
                        "Ooops2: ${e.message()}",
                        e
                    )
                )

            } catch (t: Throwable) {
                socialFacebookLiveData.emit(
                    ResultRequest.error(
                        "Ooops3: ${t.message}",
                        t
                    )
                )
            }
        }
    }
    fun getSocialLiveData(): StateFlow<ResultRequest<Any>> = socialLiveData
    fun getSocialFacebookLiveData(): StateFlow<ResultRequest<Any>> = socialFacebookLiveData

}