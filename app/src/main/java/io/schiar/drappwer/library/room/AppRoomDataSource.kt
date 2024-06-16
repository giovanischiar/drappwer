package io.schiar.drappwer.library.room

import io.schiar.drappwer.model.App
import io.schiar.drappwer.model.AppDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppRoomDataSource(private val appDAO: AppDAO): AppDataSource {
    override suspend fun create(app: App) {
        appDAO.insert(AppEntity(name = app.name, packageName = app.packageName, icon = app.icon))
    }

    override fun retrieve(): Flow<List<App>> {
        return appDAO.select().map {
            appEntities -> appEntities.map { appEntity ->
                App(
                    name = appEntity.name,
                    packageName = appEntity.packageName,
                    icon = appEntity.icon,
                    selected = appEntity.selected
                )
            }
        }
    }

    override fun retrieveSelected(): Flow<List<App>> {
        return appDAO.selectSelected().map { appEntities ->
            appEntities.map { appEntity ->
                App(
                    name = appEntity.name,
                    packageName = appEntity.packageName,
                    icon = appEntity.icon,
                    selected = appEntity.selected
                )
            }
        }
    }

    override suspend fun update(app: App) {
        appDAO.update(
            AppEntity(
                name = app.name,
                packageName = app.packageName,
                icon = app.icon,
                selected = app.selected
            )
        )
    }
}