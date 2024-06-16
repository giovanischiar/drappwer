package io.schiar.drappwer.model.local

import io.schiar.drappwer.model.SelectedAppIndexDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SelectedAppIndexLocalDataSource: SelectedAppIndexDataSource {
    private val selectedAppIndicesStateFlow = MutableStateFlow<List<Int>>(emptyList())

    override fun retrieve(): Flow<List<Int>> {
        return selectedAppIndicesStateFlow
    }

    override suspend fun update(index: Int) {
        val indices = selectedAppIndicesStateFlow.value.toMutableList()
        if (indices.contains(index)) {
            indices.remove(index)
        } else {
            indices.add(index)
        }
        selectedAppIndicesStateFlow.update { indices }
    }
}