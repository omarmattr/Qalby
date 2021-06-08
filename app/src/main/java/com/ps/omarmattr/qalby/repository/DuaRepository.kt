package com.ps.omarmattr.qalby.repository

import android.util.Log
import com.ps.omarmattr.qalby.model.solahTime.SendParam
import com.ps.omarmattr.qalby.network.DuaInterface
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
class DuaRepository @Inject constructor(val duaInterface: DuaInterface) {

    private val listDuaMStateFlow: MutableStateFlow<ResultRequest<Any>> =
        MutableStateFlow(ResultRequest.loading(Any()))

    fun getDua(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = duaInterface.getDua(
                id
            )
            try {
                if (response.isSuccessful) {
                    try {
                        Log.e(this.javaClass.name, "getPrayerTimes")

                        response.body()?.let {
                            listDuaMStateFlow.emit(ResultRequest.success(it))
                        }
                    } catch (e: Exception) {
                        ResultRequest.error(
                            "Ooops1: ${e.message}",
                            e
                        )
                    }
                } else {
                    listDuaMStateFlow.emit(
                        ResultRequest.error(
                            "Ooops2: ${response.errorBody()!!.charStream().readText()}", ""
                        )
                    )
                }
            } catch (e: HttpException) {
                listDuaMStateFlow.emit(
                    ResultRequest.error(
                        "Ooops3: ${e.message()}",
                        e
                    )
                )

            } catch (t: Throwable) {
                listDuaMStateFlow.emit(
                    ResultRequest.error(
                        "Ooops: ${t.message}",
                        t
                    )
                )
            }
        }
    }

    val listDuaStateFlow: StateFlow<ResultRequest<Any>> = listDuaMStateFlow

}