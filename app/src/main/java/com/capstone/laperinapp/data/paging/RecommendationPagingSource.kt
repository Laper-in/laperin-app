package com.capstone.laperinapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.data.retrofit.ApiService

class RecommendationPagingSource(private val apiService: ApiService, val name: String): PagingSource<Int, DataItemRecipes>() {
    override fun getRefreshKey(state: PagingState<Int, DataItemRecipes>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemRecipes> {
        return try {
            val position = params.key ?: INITIAL_PAGE
            val response = apiService.getRecommendation(name, position, params.loadSize).data

            LoadResult.Page(
                data = response,
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                nextKey = if (response.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE = 1
    }
}