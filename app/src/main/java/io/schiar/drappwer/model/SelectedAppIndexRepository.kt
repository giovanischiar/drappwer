package io.schiar.drappwer.model

class SelectedAppIndexRepository(private val appDataSource: SelectedAppIndexDataSource) {
    val selectedAppsIndicesFlow = appDataSource.retrieve()

    suspend fun selectAppOf(index: Int) {
        appDataSource.update(index)
    }
}