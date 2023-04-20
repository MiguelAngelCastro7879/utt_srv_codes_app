package com.example.codeapp.services.LoginService

import com.google.gson.annotations.SerializedName

data class QrResponse (
    @SerializedName("access_token") var access_token: String
){

    fun getAccessToken(): String {
        return access_token;
    }
}
