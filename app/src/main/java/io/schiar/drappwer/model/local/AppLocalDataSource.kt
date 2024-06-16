package io.schiar.drappwer.model.local

import io.schiar.drappwer.model.App
import io.schiar.drappwer.model.AppDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class AppLocalDataSource: AppDataSource {
    private val appsStateFlow = MutableStateFlow<List<App>>(emptyList())

    override suspend fun create(app: App) {
        val apps = appsStateFlow.value.toMutableList()
        val indexOfApp = apps.indexOf(app)
        if (indexOfApp >= 0) return
        apps.add(app)
        appsStateFlow.update { apps }
    }

    override fun retrieve(): Flow<List<App>> {
        return appsStateFlow
    }

    override fun retrieveSelected(): Flow<List<App>> {
        return appsStateFlow.map { apps -> apps.filter { it.selected } }
    }

    override suspend fun update(app: App) {
        val apps = appsStateFlow.value.toMutableList()
        val indexOfApp = apps.map { it.packageName }.indexOf(app.packageName)
        if (indexOfApp < 0) return
        apps[indexOfApp] = app
        appsStateFlow.update { apps }
    }
}