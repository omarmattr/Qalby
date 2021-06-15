package com.ps.omarmattr.qalby.di


import com.ps.omarmattr.qalby.network.DuaInterface
import com.ps.omarmattr.qalby.network.LocationInterface
import com.ps.omarmattr.qalby.network.SocialInterface
import com.ps.omarmattr.qalby.network.SolahInterface
import com.ps.omarmattr.qalby.other.BASE_LOCATION_URL
import com.ps.omarmattr.qalby.other.BASE_URL
import com.ps.omarmattr.qalby.other.BASE_URL_Qalby
import com.ps.omarmattr.qalby.other.BASE_URL_Qalby2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun InstaceRetrofit(baseUrl: String) =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .apply {
                val builder = OkHttpClient.Builder()
                builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                    .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                    .readTimeout(5, TimeUnit.MINUTES) // read timeout
                this.client(builder.build())
            }
            .build()


    @Provides
    @Singleton
    fun locationInterface() =
        InstaceRetrofit(BASE_LOCATION_URL).create(LocationInterface::class.java)

    @Provides
    @Singleton
    fun solahInterface() =
        InstaceRetrofit(BASE_URL).create(SolahInterface::class.java)

    @Provides
    @Singleton
    fun duaInterface() =
        InstaceRetrofit(BASE_URL_Qalby).create(DuaInterface::class.java)

    @Provides
    @Singleton
    fun socialInterface() =
        InstaceRetrofit(BASE_URL_Qalby2).create(SocialInterface::class.java)


}