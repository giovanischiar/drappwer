package io.schiar.drappwer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.schiar.drappwer.model.AppRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AppViewModel(private val appRepository: AppRepository): ViewModel() {
    val appsFlow = appRepository.appsFlow.map { apps -> apps.map { app -> app.toViewData() } }
    val selectedAppsFlow = appRepository.appsFlow
        .map { apps -> apps.filter { it.selected }.map { app -> app.toViewData() } }

    fun addAppOf(name: String, packageName: String, icon: ByteArray) = viewModelScope.launch {
        appRepository.addAppOf(name = name, packageName = packageName, icon = icon)
    }

    fun selectAppOf(index: Int) = viewModelScope.launch {
        appRepository.selectAppOf(index = index)
    }
}