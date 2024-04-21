package com.example.taskplanner.view.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taskplanner.data.model.repository.ListTaskRepository
import com.example.taskplanner.data.model.Day
import javax.inject.Inject

class CalendarPagingSource @Inject constructor(private val listTaskRepository: ListTaskRepository) :
    PagingSource<Int, Day>() {
    override fun getRefreshKey(state: PagingState<Int, Day>): Int {

        if (state.anchorPosition != null) {
            return state.anchorPosition.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition!!)
                anchorPage!!.prevKey!!.plus(1)
            }
        }
        return FIRST_PAGE
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Day> {
        val page = params.key ?: FIRST_PAGE
        val count = page * 50

        return kotlin.runCatching {
            listTaskRepository.getPageDays(count)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = page - 1,
                    nextKey = page + 1,
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
