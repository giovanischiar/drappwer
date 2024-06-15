package io.schiar.drappwer.view.apps

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import io.schiar.drappwer.view.theme.DrappwerTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppsScreen() {
    val context = LocalContext.current
    val pm = remember { context.packageManager }
    val mainIntent = remember { Intent(Intent.ACTION_MAIN, null) }
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    var resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        pm.queryIntentActivities(
            mainIntent,
            PackageManager.ResolveInfoFlags.of(0L)
        )

    } else {
        pm.queryIntentActivities(mainIntent, 0)
    }

    val apps = resolvedInfos.map { resolvedInfo ->
        val resources =  pm.getResourcesForApplication(resolvedInfo.activityInfo.applicationInfo)
        val appName = if (resolvedInfo.activityInfo.labelRes != 0) {
            resources.getString(resolvedInfo.activityInfo.labelRes)
        } else {
            // getting it out of app info - equivalent to context.packageManager.getApplicationInfo
            resolvedInfo.activityInfo.applicationInfo.loadLabel(pm).toString()
        }
        val iconDrawable = resolvedInfo.activityInfo.loadIcon(pm)
        return@map Pair(appName, iconDrawable)
    }

    val appsSelected = remember { mutableStateListOf<Int>() }

    DrappwerTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Column(modifier = Modifier.fillMaxSize().padding(all = 30.dp)) {
                FlowRow {
                    appsSelected.map {index ->
                        val (_, iconDrawable) = apps[index]
                        Image(
                            modifier = Modifier.size(20.dp),
                            bitmap = iconDrawable.toBitmap().asImageBitmap(),
                            contentDescription = ""
                        )
                    }
                }

                LazyColumn {
                    items(count = resolvedInfos.size) { index ->
                        val (appName, iconDrawable) = apps[index]
                        Row(modifier = Modifier.clickable {
                            val indexOfIndex = appsSelected.indexOf(index)
                            if (indexOfIndex < 0) {
                                appsSelected.add(index)
                            } else {
                                appsSelected.removeAt(indexOfIndex)
                            }
                        }) {
                            Image(
                                modifier = Modifier.size(20.dp),
                                bitmap = iconDrawable.toBitmap().asImageBitmap(),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = appName)
                        }
                    }
                }
            }
        }
    }
}