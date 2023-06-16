package com.vie.healthyteeth.api

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class FileUploadResponse(

    @field:SerializedName("diagnosis")
    val diagnosis: String
)

interface ApiService {
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY4NjgxNzIxNiwianRpIjoiNzZhMjEwZTEtZDZjNS00Mzg2LWJkMzItMGQyMGU5MmY4NTcyIiwidHlwZSI6ImFjY2VzcyIsInN1YiI6ImVtYWlsIiwibmJmIjoxNjg2ODE3MjE2LCJleHAiOjE2OTQ1OTMyMTZ9.63lLPxRi7B8i8M3tZAOP-u6ftBa4LB85gCe7EECNOKM")
    @POST("predict")
    fun uploadImage(
        @Query ("title") title: String,
        @Query ("body") body: String,
    ): retrofit2.Call<FileUploadResponse>
}


class ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client =  OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .callTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .connectTimeout(10, TimeUnit.MINUTES)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://heeltyteeth-4vz5wjadpa-uc.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}