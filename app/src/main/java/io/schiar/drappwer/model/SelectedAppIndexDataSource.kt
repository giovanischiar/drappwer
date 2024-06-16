package io.schiar.drappwer.model

import kotlinx.coroutines.flow.Flow

interface SelectedAppIndexDataSource {
    fun retrieve(): Flow<List<Int>>
    suspend fun update(index: Int)
}