package com.ps.omarmattr.qalby.network

import com.ps.omarmattr.qalby.model.home.social.Social
import retrofit2.Response
import retrofit2.http.GET

interface SocialInterface {
    @GET("qalby-share/facebook")
    suspend fun getFacebook():Response<Social>

    @GET("qalby-share/instagram")
    suspend fun getInstagram():Response<Social>
}