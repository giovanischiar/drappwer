package io.schiar.drappwer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.schiar.drappwer.model.App
import io.schiar.drappwer.model.SelectedAppIndexRepository
import io.schiar.drappwer.view.shared.viewdata.AppViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(private val appRepository: SelectedAppIndexRepository): ViewModel() {
    private val apps: MutableList<App> = mutableListOf()
    private val appsMutableStateFlow: MutableStateFlow<List<AppViewData>> by lazy {
        MutableStateFlow(apps.map { it.toViewData() })
    }
    val appsFlow: Flow<List<AppViewData>> = appsMutableStateFlow
    val selectedAppsIndicesFlow: Flow<List<Int>> = appRepository.selectedAppsIndicesFlow

    fun addAppOf(name: String, packageName: String, icon: ByteArray) {
        val app = App(name = name, packageName = packageName, icon = icon)
        apps.add(app)
        appsMutableStateFlow.update { apps.map { it.toViewData() } }
    }

    fun selectAppOf(index: Int) {
        viewModelScope.launch { appRepository.selectAppOf(index = index) }
    }
}