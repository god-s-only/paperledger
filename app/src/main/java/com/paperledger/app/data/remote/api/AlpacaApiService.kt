package com.paperledger.app.data.remote.api

import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO
import com.paperledger.app.data.remote.dto.account.response.success.AccountResponseDTO
import com.paperledger.app.data.remote.dto.ach.request.ACHRelationshipsRequestDTO
import com.paperledger.app.data.remote.dto.ach.response.success.ACHRelationshipResponseDTO
import com.paperledger.app.data.remote.dto.ach_get.GetACHRelationshipsDTO
import com.paperledger.app.data.remote.dto.assets.AssetsResponseDTO
import com.paperledger.app.data.remote.dto.funding.request.FundingRequestDTO
import com.paperledger.app.data.remote.dto.funding.response.success.FundingResponseDTO
import com.paperledger.app.data.remote.dto.watchlists_get.GetWatchlistsDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AlpacaApiService {

    @GET("/v1/assets")
    suspend fun getAllAssets(
        @Query("status") status: String = "all",
        @Query("asset_class") assetClass: String = "us_equity",
        @Query("attributes") attributes: String = "ptp_no_exception,ipo"
    ): Response<AssetsResponseDTO>

    @POST("/v1/accounts")
    suspend fun createAccount(
        @Body accountRequest: AccountRequestDTO
    ): Response<AccountResponseDTO>

    @PATCH("/v1/accounts/{account_id}")
    suspend fun updateAccount(
        @Path("account_id") accountId: String,
        @Body accountRequest: AccountRequestDTO
    ): Response<AccountResponseDTO>

    @POST("/v1/accounts/{account_id}/ach_relationships")
    suspend fun createACHRelationship(
        @Path("account_id") accountId: String,
        @Body achRelationshipRequest: ACHRelationshipsRequestDTO
    ): Response<ACHRelationshipResponseDTO>

    @POST("/v1/accounts/{account_id}/transfers")
    suspend fun requestTransfer(
        @Path("account_id") accountId: String,
        @Body fundingRequestDTO: FundingRequestDTO
    ): Response<FundingResponseDTO>

    @GET("/v1/accounts/{account_id}/ach_relationships")
    suspend fun getACHRelationship(
        @Path("account_id") accountId: String
    ): Response<GetACHRelationshipsDTO>

    @GET("/v1/trading/accounts/{account_id}/watchlists")
    suspend fun getWatchlists(
        @Path("account_id") accountId: String
    ): Response<GetWatchlistsDTO>
}