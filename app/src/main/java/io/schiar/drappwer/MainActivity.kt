/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package io.schiar.drappwer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import io.schiar.drappwer.library.room.AppRoomDataSource
import io.schiar.drappwer.library.room.DrappwerRoomDatabase
import io.schiar.drappwer.model.AppRepository
import io.schiar.drappwer.model.local.AppLocalDataSource
import io.schiar.drappwer.view.apps.AppsScreen
import io.schiar.drappwer.viewmodel.AppViewModel
import io.schiar.drappwer.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        val appDAO = DrappwerRoomDatabase.getDatabase(context = applicationContext).appDAO()
        val viewModelProvider = ViewModelProvider(
            this,
            ViewModelFactory(
                appRepository = AppRepository(appDataSource = AppRoomDataSource(appDAO = appDAO))
            )
        )
        setContent { AppsScreen(viewModel = viewModelProvider[AppViewModel::class.java]) }
    }

    @Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
    @Composable
    fun AppsScreenPreview() {
        AppsScreen(viewModel = AppViewModel(
                appRepository = AppRepository(appDataSource = AppLocalDataSource())
            )
        )
    }
}

