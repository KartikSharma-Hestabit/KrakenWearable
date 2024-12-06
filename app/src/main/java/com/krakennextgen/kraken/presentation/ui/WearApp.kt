package com.krakennextgen.kraken.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.krakennextgen.kraken.presentation.viewModels.WearableViewModel

@Composable
fun WearApp(viewModel: WearableViewModel = viewModel()) {
    val isConnected by viewModel.isConnected.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        TimeText()
        if (!isConnected) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp),

                ) {
                Text(
                    text = "Please connect your watch to phone by Bluetooth",
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 3.dp,end = 3.dp,
                        bottom = 5.dp)
                )
                RetryCheck(viewModel)
            }
        }
    }
}


@Composable
fun RetryCheck(viewModel: WearableViewModel) {
    Card(
        onClick = { viewModel.checkBluetoothConnection() },
        modifier = Modifier.padding(5.dp),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                modifier = Modifier.padding(end = 8.dp) // Add spacing between icon and text
            )
            Text(
                text = "Retry Connection",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
private  fun DefaultPreview() {
    WearApp()
}
