package com.example.aihousekeeper.utils

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object APIService {
    private const val BASE_URL = "http://10.0.2.2:8888/"

    fun getService(): APIConsumer{
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setFieldNamingStrategy(SnakeCaseNamingStrategy())
            .create()

        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit = builder.build()
        return retrofit.create(APIConsumer::class.java)
    }
}