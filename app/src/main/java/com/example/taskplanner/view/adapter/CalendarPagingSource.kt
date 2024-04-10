package com.example.taskplanner.view.adapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taskplanner.data.Repository
import com.example.taskplanner.data.model.Day
import javax.inject.Inject

class CalendarPagingSource @Inject constructor(private val repository: Repository) :
    PagingSource<Int, Day>() {
    override fun getRefreshKey(state: PagingState<Int, Day>): Int {
        return FIRST_PAGE
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Day> {
        val page = params.key ?: FIRST_PAGE
        val count = page * 10
        return kotlin.runCatching {
            repository.getPageDays(count)
        }.fold(
            onSuccess = {
                Log.d("@@@", "list =$it")
                LoadResult.Page(
                    data = it,
                    prevKey = page - 1,
                    nextKey = page + 1
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }

    companion object {
        private const val FIRST_PAGE = 0
    }
}
