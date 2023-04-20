package com.example.codeapp.services.LoginService

import com.example.codeapp.services.Singleton
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface QrService {
    @POST("api/verify/qrcode")
    fun validateCode(@Header("Authorization") token: String): Call<QrResponse>
}