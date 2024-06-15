/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package io.schiar.drappwer.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import io.schiar.drappwer.R
import io.schiar.drappwer.presentation.theme.DrappwerTheme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WearApp() {
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

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}