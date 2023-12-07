package com.capstone.laperinapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.data.response.DonationsItem
import com.capstone.laperinapp.data.retrofit.ApiService

class DonationsPagingSource(private val apiService: ApiService): PagingSource<Int, DonationsItem>() {
    override fun getRefreshKey(state: PagingState<Int, DonationsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DonationsItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllDonation(position, params.loadSize).donations

            LoadResult.Page(
                data = response,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1
        const val TAG = "DonationsPagingSource"
    }
}