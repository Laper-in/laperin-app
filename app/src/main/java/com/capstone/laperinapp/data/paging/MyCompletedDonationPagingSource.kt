package com.capstone.laperinapp.data.paging

import androidx.paging.PagingSource
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.data.retrofit.ApiService

class MyCompletedDonationPagingSource(private val apiService: ApiService): PagingSource<Int, DataItemDonation>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemDonation> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getDonationByUser(position, params.loadSize).data

            val uncompletedDonation = response.filter { it.isDone }

            LoadResult.Page(
                data = uncompletedDonation,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: androidx.paging.PagingState<Int, DataItemDonation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}