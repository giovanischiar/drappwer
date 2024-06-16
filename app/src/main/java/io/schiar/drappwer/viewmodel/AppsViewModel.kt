package io.schiar.drappwer.viewmodel

import androidx.lifecycle.ViewModel
import io.schiar.drappwer.model.App
import io.schiar.drappwer.view.shared.viewdata.AppViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AppsViewModel: ViewModel() {
    private val apps: MutableList<App> = mutableListOf()
    private var selectedAppsIndices: List<Int> = mutableListOf()

    private val appsMutableStateFlow: MutableStateFlow<List<AppViewData>> by lazy {
        MutableStateFlow(apps.map { it.toViewData() })
    }
    val appsFlow: Flow<List<AppViewData>> = appsMutableStateFlow

    private val selectedAppsIndicesStateFlow: MutableStateFlow<List<Int>> by lazy {
        MutableStateFlow(selectedAppsIndices)
    }
    val selectedAppsIndicesFlow: Flow<List<Int>> = selectedAppsIndicesStateFlow

    fun addAppOf(name: String, packageName: String, icon: ByteArray) {
        val app = App(name = name, packageName = packageName, icon = icon)
        apps.add(app)
        appsMutableStateFlow.update { apps.map { it.toViewData() } }
    }

    fun selectAppOf(index: Int) {
        val mutableList = selectedAppsIndices.toMutableList()
        val indexOfApp = selectedAppsIndices.indexOf(index)
        if (indexOfApp < 0) {
            mutableList.add(index)
        } else {
            mutableList.removeAt(indexOfApp)
        }
        selectedAppsIndices = mutableList
        selectedAppsIndicesStateFlow.update { selectedAppsIndices }
    }
}