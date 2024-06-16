package io.schiar.drappwer.library.tile

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.ModifiersBuilders.Clickable
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.TimelineBuilders
import androidx.wear.protolayout.material.Button
import androidx.wear.protolayout.material.CompactChip
import androidx.wear.protolayout.material.layouts.MultiButtonLayout
import androidx.wear.protolayout.material.layouts.PrimaryLayout
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.tools.LayoutRootPreview
import com.google.android.horologist.compose.tools.buildDeviceParameters
import com.google.android.horologist.tiles.SuspendingTileService
import io.schiar.drappwer.library.room.AppRoomDataSource
import io.schiar.drappwer.library.room.DrappwerRoomDatabase
import io.schiar.drappwer.model.App
import io.schiar.drappwer.model.AppDataSource
import kotlinx.coroutines.flow.first

private const val RESOURCES_VERSION = "0"

@OptIn(ExperimentalHorologistApi::class)
class MainTileService : SuspendingTileService() {

    private lateinit var appDataSource: AppDataSource
    private var selectedApps: List<App> = emptyList()

    override fun onCreate() {
        super.onCreate()
        val appDAO = DrappwerRoomDatabase.getDatabase(applicationContext).appDAO()
        appDataSource = AppRoomDataSource(appDAO = appDAO)
    }

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ResourceBuilders.Resources {
        val resourceBuilders = ResourceBuilders.Resources.Builder()
        selectedApps.forEach { app ->
            resourceBuilders.addIdToImageMapping(
                app.packageName,
                ResourceBuilders.ImageResource.Builder().setInlineResource(
                    ResourceBuilders
                        .InlineImageResource.Builder()
                        .setData(app.icon)
                        .setFormat(ResourceBuilders.IMAGE_FORMAT_UNDEFINED) //This is required unless you know the format, I recommend leaving this way
                        .setHeightPx(20 * 2) //2 for better quality
                        .setWidthPx(50 * 2) //2 for better quality
                        .build()
                ).build()
            )
        }

        return resourceBuilders.setVersion(RESOURCES_VERSION).build()
    }

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ): TileBuilders.Tile {
        val lastClickableId = requestParams.currentState.lastClickableId
        if (lastClickableId.contains(other = "open")) {
            val applicationID = lastClickableId.split(" ")[1]
            val launchIntent = packageManager.getLaunchIntentForPackage(applicationID)
            if (launchIntent != null) {
                startActivity(this, launchIntent, Bundle.EMPTY)
            }
        }
        selectedApps = appDataSource.retrieveSelected().first()
        val singleTileTimeline = TimelineBuilders.Timeline.Builder().addTimelineEntry(
            TimelineBuilders.TimelineEntry.Builder().setLayout(
                LayoutElementBuilders.Layout.Builder().setRoot(
                    tileLayout(context = this, apps = selectedApps)
                ).build()
            ).build()
        ).build()

        return TileBuilders.Tile.Builder().setResourcesVersion(RESOURCES_VERSION)
            .setTileTimeline(singleTileTimeline).build()
    }
}

private fun tileLayout(context: Context, apps: List<App>): LayoutElementBuilders.LayoutElement {
    var multiButtonLayoutBuilder = MultiButtonLayout.Builder()
    apps.forEach { app ->
        val clickable = Clickable.Builder()
            .setId("open ${app.packageName}")
            .setOnClick(ActionBuilders.LoadAction.Builder().build())
            .build()

        multiButtonLayoutBuilder = multiButtonLayoutBuilder
            .addButtonContent(
                Button.Builder(context, clickable)
                    .setIconContent(app.packageName)
                    .setContentDescription(app.name)
                    .build()
            )
    }
    val deviceParameters = buildDeviceParameters(context.resources)

    val clickable = Clickable.Builder()
        .setId("open io.schiar.drappwer")
        .setOnClick(ActionBuilders.LoadAction.Builder().build())
        .build()

    if (apps.isEmpty()) {
        return PrimaryLayout.Builder(deviceParameters)
            .setContent(
                CompactChip.Builder(context, "Add apps",  clickable, deviceParameters)
                    .build()
            )
            .build()
    }

    return PrimaryLayout.Builder(deviceParameters)
        .setContent(multiButtonLayoutBuilder.build())
        .setPrimaryChipContent(
            CompactChip.Builder(context, "Edit",  clickable, deviceParameters)
                .build()
        )
        .build()
}

@Preview(
    device = Devices.WEAR_OS_SMALL_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)
@Composable
fun TilePreview() {
    LayoutRootPreview(root = tileLayout(LocalContext.current, emptyList()))
}