package io.schiar.drappwer.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class AppRepository(private val appDataSource: AppDataSource) {
    private var apps = emptyList<App>()
    private var overcapacityStateFlow = MutableStateFlow(false)
    val appsFlow = appDataSource.retrieve().onEach {apps = it }
    var selectedAppsFlow = appDataSource.retrieveSelected().onEach { selectedApps ->
        if (selectedApps.size > 5) {
            overcapacityStateFlow.update { true }
        } else {
            overcapacityStateFlow.update { false }
        }
    }
    var overcapacityFlow: Flow<Boolean> = overcapacityStateFlow

    suspend fun addAppOf(name: String, packageName: String, icon: ByteArray) {
        appDataSource.create(app = App(name = name, packageName = packageName, icon = icon))
    }

    suspend fun selectAppOf(index: Int) {
        val app = apps.getOrNull(index = index) ?: return
        appDataSource.update(app.switch())
    }
}