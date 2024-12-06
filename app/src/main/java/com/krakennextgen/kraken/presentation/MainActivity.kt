/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.krakennextgen.kraken.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.TimeText
import com.krakennextgen.kraken.presentation.theme.KrakenWearableTheme
import com.krakennextgen.kraken.presentation.ui.WearApp
import com.krakennextgen.kraken.presentation.ui.WearableScreen
import com.krakennextgen.kraken.presentation.viewModels.WearableViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: WearableViewModel by viewModels()

    val REQUEST_BLUETOOTH_PERMISSION = 1
    @SuppressLint("InlinedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }
        else{
            viewModel.permissionsGranted.value = true
        }

        setContent {
            KrakenWearableTheme {
                TimeText(timeTextStyle = TextStyle(fontSize = 10.sp))
                if (viewModel.permissionsGranted.value == true) {
                    WearableScreen(viewModel)
                } else {
                    WearApp(viewModel = viewModel)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_BLUETOOTH_PERMISSION)
        {
            viewModel.permissionsGranted.value =
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        }
    }
}
