package com.paperledger.app.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface AlpacaApiService {

    @GET("/v1/assets")
    suspend fun getAllAssets()
}