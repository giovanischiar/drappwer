package io.schiar.drappwer.model

import kotlinx.coroutines.flow.onEach

class AppRepository(private val appDataSource: AppDataSource) {
    private var apps = emptyList<App>()
    val appsFlow = appDataSource.retrieve().onEach { apps = it }
    var selectedAppsFlow = appDataSource.retrieveSelected()

    suspend fun addAppOf(name: String, packageName: String, icon: ByteArray) {
        appDataSource.create(app = App(name = name, packageName = packageName, icon = icon))
    }

    suspend fun selectAppOf(index: Int) {
        val app = apps.getOrNull(index = index) ?: return
        appDataSource.update(app.switch())
    }
}