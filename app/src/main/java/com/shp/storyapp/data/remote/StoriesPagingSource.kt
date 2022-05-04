package com.shp.storyapp.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shp.storyapp.data.model.DataStory


class StoriesPagingSource(private val storyApiService: StoryApiService, private val token: String) :
    PagingSource<Int, DataStory>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, DataStory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataStory> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = storyApiService.getAllStories("Bearer $token", page, params.loadSize).listStory
            LoadResult.Page(
                data = responseData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}
