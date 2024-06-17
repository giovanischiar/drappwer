package io.schiar.drappwer.view.apps

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.ExperimentalWearFoundationApi
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyColumnDefaults
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rememberActiveFocusRequester
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipColors
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import io.schiar.drappwer.view.shared.theme.DrappwerTheme
import io.schiar.drappwer.view.shared.toBitmap
import io.schiar.drappwer.view.shared.toByteArray
import io.schiar.drappwer.viewmodel.AppViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalWearFoundationApi::class)
@Composable
fun AppsScreen(viewModel: AppViewModel) {
    val context = LocalContext.current
    val pm = remember { context.packageManager }
    val mainIntent = remember { Intent(Intent.ACTION_MAIN, null) }
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val apps by viewModel.appsFlow.collectAsState(initial = emptyList())
    val selectedApps by viewModel.selectedAppsFlow.collectAsState(initial = emptyList())

    val resolvedInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        pm.queryIntentActivities(
            mainIntent,
            PackageManager.ResolveInfoFlags.of(0L)
        )
    } else {
        pm.queryIntentActivities(mainIntent, 0)
    }

    resolvedInfoList.forEach { resolvedInfo ->
        val resources =  pm.getResourcesForApplication(resolvedInfo.activityInfo.applicationInfo)
        val appName = if (resolvedInfo.activityInfo.labelRes != 0) {
            resources.getString(resolvedInfo.activityInfo.labelRes)
        } else {
            resolvedInfo.activityInfo.applicationInfo.loadLabel(pm).toString()
        }

        val packageName = resolvedInfo.activityInfo.packageName ?: ""
        val iconDrawable = resolvedInfo.activityInfo.loadIcon(pm)
        viewModel.addAppOf(
            name = appName,
            packageName = packageName,
            icon = iconDrawable.toByteArray()
        )
    }

    val listState = rememberScalingLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = rememberActiveFocusRequester()

    DrappwerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Box(modifier = Modifier.fillMaxSize()) {
                ScalingLazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .onRotaryScrollEvent {
                            println("rotary scroll event: $it")
                            coroutineScope.launch { listState.scrollBy(it.verticalScrollPixels) }
                            true
                        }
                        .focusRequester(focusRequester)
                        .focusable(),
                    autoCentering = AutoCenteringParams(itemIndex = 0),
                    state = listState,
                    flingBehavior = ScalingLazyColumnDefaults.snapFlingBehavior(
                        state = listState,
                        snapOffset = 0.dp
                    )
                ) {
                    items(count = apps.size) { index ->
                        val (appName, icon, selected) = apps[index]
                        Chip(
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = appName,
                                    color = if (selected) Color.Black else Color.White
                                )
                            },
                            onClick = {
                                viewModel.selectAppOf(index = index)
                            },
                            icon = {
                                Image(
                                    modifier = Modifier.size(20.dp),
                                    bitmap = icon.toBitmap().asImageBitmap(),
                                    contentDescription = ""
                                )
                            },
                            colors = ChipDefaults.chipColors(
                                backgroundColor = if (selected) Color.Yellow else MaterialTheme.colors.surface
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    contentAlignment = Alignment.TopCenter) {
                    FlowRow {
                        selectedApps.map { app ->
                            val (_, icon) = app
                            Image(
                                modifier = Modifier.size(20.dp),
                                bitmap = icon.toBitmap().asImageBitmap(),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}