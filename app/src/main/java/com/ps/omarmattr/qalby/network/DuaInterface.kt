package com.ps.omarmattr.qalby.network

import com.ps.omarmattr.qalby.model.dua.DuaRequestItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DuaInterface {

    @GET("dua/public/{id}")
    suspend fun getDua(@Path("id") id: Int): Response<List<DuaRequestItem>>
}
