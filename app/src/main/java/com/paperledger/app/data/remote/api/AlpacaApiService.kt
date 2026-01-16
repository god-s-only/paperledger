package com.paperledger.app.data.remote.api

import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO
import com.paperledger.app.data.remote.dto.account.response.AccountResponseDTO
import com.paperledger.app.data.remote.dto.assets.AssetsResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AlpacaApiService {

    @GET("/v1/assets")
    suspend fun getAllAssets(
        @Query("status") status: String = "all",
        @Query("asset_class") assetClass: String = "us_equity"
    ): Response<AssetsResponseDTO>

    @POST("/v1/accounts")
    suspend fun createAccount(
        @Body accountRequest: AccountRequestDTO
    ): Response<AccountResponseDTO>
}