package com.example.taskplanner.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taskplanner.domain.Day
import java.time.LocalDate
import javax.inject.Inject


class CalendarPagingSource @Inject constructor(private val repositoryImpl: RepositoryImpl) : PagingSource<Int, Day>() {
    override fun getRefreshKey(state: PagingState<Int, Day>): Int {
        return FIRST_PAGE
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Day> {
        val page = params.key ?: FIRST_PAGE
        Log.d("@@@", "page =$page")
        return kotlin.runCatching {
            val count = page * 10
            repositoryImpl.getPageDays(count)
        }.fold(
            onSuccess = {
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
        private const val FIRST_PAGE = 1
    }
}
