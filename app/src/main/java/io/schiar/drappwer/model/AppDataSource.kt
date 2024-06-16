package io.schiar.drappwer.model

import kotlinx.coroutines.flow.Flow

interface AppDataSource {
    suspend fun create(app: App)
    fun retrieve(): Flow<List<App>>
    suspend fun update(app: App)
}