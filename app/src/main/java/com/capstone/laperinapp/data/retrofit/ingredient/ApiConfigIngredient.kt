package com.capstone.laperinapp.data.retrofit.ingredient

import com.capstone.laperinapp.BuildConfig
import com.capstone.laperinapp.data.retrofit.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfigIngredient {

    companion object {
        fun getApiServiceIngredient(token: String): ApiServiceIngredient {
            val loggingInterceptor =
                if (BuildConfig.DEBUG){
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                }else{
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL_INGREDIENTS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiServiceIngredient::class.java)
        }
    }
}