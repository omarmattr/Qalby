package com.ps.omarmattr.qalby.network

import com.ps.omarmattr.qalby.model.solahTime.SolahTime
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SolahInterface {

    @GET("calendar")
    suspend fun getPrayerTimes(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int,
        @Query("month") month: Int,
        @Query("year") year: Int,
    ): Response<SolahTime>

    @GET("calendarByAddress")
    suspend fun getPrayerAddress(
        @Query("address") address: String,
        @Query("method") method: Int,
        @Query("month") month: Int,
        @Query("year") year: Int,
    ): Response<SolahTime>
}