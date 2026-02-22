package com.paperledger.app.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.paperledger.app.data.mappers.assets.toAssetsDomain
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.domain.models.assets.AssetsModel
import org.json.JSONObject
import javax.inject.Inject

class AssetsPagingSource @Inject constructor(
    private val alpacaApiService: AlpacaApiService,
    private val searchQuery: String = ""
) : PagingSource<Int, AssetsModel>() {

    private var cachedAssets: List<AssetsModel>? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AssetsModel> {
        val page = params.key ?: 0
        return try {
            val allAssets = cachedAssets ?: run {
                val response = alpacaApiService.getAllAssets()
                if (!response.isSuccessful) {
                    val errorMessage = response.errorBody()?.string()?.let {
                        runCatching { JSONObject(it).getString("message") }.getOrDefault("Error fetching assets")
                    } ?: "Error fetching assets"
                    return LoadResult.Error(Exception(errorMessage))
                }
                val body = response.body()
                    ?: return LoadResult.Error(Exception("Empty response body"))

                body.map { it.toAssetsDomain() }
                    .filter { asset ->
                        searchQuery.isBlank() ||
                                asset.symbol.contains(searchQuery, ignoreCase = true) ||
                                asset.name.contains(searchQuery, ignoreCase = true)
                    }
                    .also { cachedAssets = it }
            }

            val fromIndex = page * params.loadSize
            if (fromIndex >= allAssets.size) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = null
                )
            }

            val toIndex = minOf(fromIndex + params.loadSize, allAssets.size)
            val pageData = allAssets.subList(fromIndex, toIndex)

            LoadResult.Page(
                data = pageData,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (toIndex >= allAssets.size) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AssetsModel>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}