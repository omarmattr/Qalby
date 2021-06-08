package com.ps.omarmattr.qalby.network

import com.ps.omarmattr.qalby.model.location.ResultLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationInterface {
    @GET("reverse")
   suspend fun getLocation(
        @Query("format") format:String = "json",
        @Query("lat") lat:Double ,
        @Query("lon") lon:Double,
        @Query("zoom") zoom:Int = 12,
        @Query("addressdetails") addressdetails:Int = 1,
        ):Response<ResultLocation>
}