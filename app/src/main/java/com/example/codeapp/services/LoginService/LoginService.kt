package com.example.codeapp.services.LoginService

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Content-Type: application/json")
    @POST("api/login-app")
    fun validateCode(@Body requestBody: HashMap<String,String>): Call<LoginResponse>
}