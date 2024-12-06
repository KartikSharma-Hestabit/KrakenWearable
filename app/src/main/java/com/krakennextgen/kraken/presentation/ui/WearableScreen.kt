package com.krakennextgen.kraken.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.tooling.preview.devices.WearDevices
import com.krakennextgen.kraken.R
import com.krakennextgen.kraken.presentation.theme.KrakenWearableTheme
import com.krakennextgen.kraken.presentation.viewModels.WearableViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WearableScreen(viewModel: WearableViewModel = WearableViewModel()) {

    val context = LocalContext.current

    val icons = listOf(
        R.drawable.mode,
        R.drawable.move_up,
        R.drawable.move_down,
        R.drawable.ok,
        R.drawable.function
    )

    CircularIconList(
        icons = icons,
        modifier = Modifier.fillMaxSize(),
        onClick = { clickedIcon ->
            viewModel.sendDataToPhone(context = context, clickedIcon)
        },
        onLongClick = { clickedIcon ->
            viewModel.sendDataToPhone(context = context, clickedIcon, true)
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircularIconList(
    icons: List<Int>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit,
    onLongClick: (Int) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {

        Button(
            onClick = {
                onClick(R.drawable.focus)
            }, modifier = Modifier
                .align(Alignment.Center)
                .size(45.dp)
        ) {
            Icon(painter = painterResource(R.drawable.focus), "")
        }

        // Calculate positions for each icon
        val angleStep = 360f / icons.size
        icons.forEachIndexed { index, iconRes ->
            val angle =
                Math.toRadians((angleStep * index).toDouble() - 90.0) // Offset by -90° to start at the top


            val radius = 8 + icons.size * 10 // Adjust radius based on icon count
            val x = radius * cos(angle)
            val y = radius * sin(angle)

            Button(
                onClick = {
                    //not needed to implement
                }, modifier = Modifier
                    .offset(x.dp, y.dp)
                    .size(45.dp)
                    .combinedClickable(
                        onClick = {
                            onClick(iconRes)
                        },
                        onLongClick = {
                            if (iconRes == R.drawable.function || iconRes == R.drawable.ok) {
                                onLongClick(iconRes)
                            }
                        }
                    )
            ) {
                Icon(painter = painterResource(iconRes), "")
            }
        }
    }
}


@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
private fun DefaultPreview() {
    KrakenWearableTheme {
        WearableScreen()
    }

}