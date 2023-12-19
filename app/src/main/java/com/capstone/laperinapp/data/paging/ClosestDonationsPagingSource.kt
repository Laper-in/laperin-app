package com.capstone.laperinapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.data.retrofit.ApiService

class ClosestDonationsPagingSource(private val apiService: ApiService, private val longitude: Float, private val latitude: Float): PagingSource<Int, DataItemDonation>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemDonation> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllClosestDonation(longitude, latitude, position, params.loadSize).data

            Log.d(TAG, "load: $response")

            LoadResult.Page(
                data = response,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "load: $e", )
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataItemDonation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object{
        const val INITIAL_PAGE_INDEX = 1
        const val TAG = "DonationsPagingSource"
    }
}