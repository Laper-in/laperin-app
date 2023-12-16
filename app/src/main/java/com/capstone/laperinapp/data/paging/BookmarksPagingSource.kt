package com.capstone.laperinapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.laperinapp.data.response.BookmarksItem
import com.capstone.laperinapp.data.retrofit.ApiService

class BookmarksPagingSource(private val apiService: ApiService, private val idUser: String):
    PagingSource<Int, BookmarksItem>() {
    override fun getRefreshKey(state: PagingState<Int, BookmarksItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookmarksItem> {
        return try {
            val position = params.key ?: STARTING_PAGE_INDEX
            val response = apiService.getBookmarks(idUser, position, params.loadSize).bookmarks

            LoadResult.Page(
                data = response,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}