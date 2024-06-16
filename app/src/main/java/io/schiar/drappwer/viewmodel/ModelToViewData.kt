package io.schiar.drappwer.viewmodel

import io.schiar.drappwer.model.App
import io.schiar.drappwer.view.shared.viewdata.AppViewData

fun App.toViewData(): AppViewData {
    return AppViewData(name = name, icon = icon)
}