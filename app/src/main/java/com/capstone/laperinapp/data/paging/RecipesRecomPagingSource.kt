package com.capstone.laperinapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.laperinapp.data.response.RecipeItem
import com.capstone.laperinapp.data.retrofit.ApiService

class RecipesRecomPagingSource(private val apiService: ApiService) : PagingSource<Int, RecipeItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllRecipes(position, params.loadSize).recipe

            val dataRandom = response.shuffled()
            val slicedData = dataRandom.slice(0 until minOf(dataRandom.size, 5))

            LoadResult.Page(
                data = slicedData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (slicedData.size < 5) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}