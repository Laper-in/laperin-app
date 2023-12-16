package com.capstone.laperinapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.response.IngredientsItem
import com.capstone.laperinapp.data.retrofit.ApiService

class IngredientsPagingSource(private val apiService: ApiService): PagingSource<Int, IngredientsItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IngredientsItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getIngredient(position, params.loadSize).recipe

            Log.i(TAG, "ingredients search: $response")

            LoadResult.Page(
                data = response,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, IngredientsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val TAG = "IngredientsPagingSource"
    }
}