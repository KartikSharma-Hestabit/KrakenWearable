package com.krakennextgen.kraken.presentation.viewModels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.gms.wearable.DataItem
import android.util.Log
import com.krakennextgen.kraken.R
import com.krakennextgen.kraken.presentation.data.SendDataType


@HiltViewModel
class WearableViewModel @Inject constructor() : ViewModel() {
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected
    val permissionsGranted = MutableLiveData(false)


    init {
        checkBluetoothConnection()
    }

    @SuppressLint("MissingPermission")
    fun checkBluetoothConnection() {
        viewModelScope.launch {
            try {
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val connectedDevices = bluetoothAdapter?.bondedDevices
                _isConnected.value = connectedDevices?.any { it.name.contains("Watch") } == true
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    @SuppressLint("InlinedApi")
    fun checkAndRequestPermissions(context: Context, onPermissionsResult: (Boolean) -> Unit) {

        viewModelScope.launch {
            val granted = listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            ).all { permission ->
                context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
            permissionsGranted.value = granted
            onPermissionsResult(granted)
        }
    }

    fun sendDataToPhone(context: Context, data: Int, isLongPressed: Boolean = false) {
        // Create the PutDataRequest with a unique path

        val action: SendDataType = when (data) {
            R.drawable.ok -> {
                if (isLongPressed)
                    SendDataType.OK_LON_PRESSED
                else
                    SendDataType.OK
            }

            R.drawable.focus -> {
                SendDataType.SHUTTER
            }

            R.drawable.mode -> {
                SendDataType.BACK
            }

            R.drawable.move_up -> {
                SendDataType.UP
            }

            R.drawable.move_down -> {
                SendDataType.DOWN
            }

            else -> {
                if (isLongPressed)
                    SendDataType.FN_LONG_PRESSED
                else
                    SendDataType.FN
            }
        }

        val putDataRequest = PutDataRequest.create("/my_data_path")
        putDataRequest.data = action.name.toByteArray() // Data to send
        // Get DataClient
        val dataClient: DataClient = Wearable.getDataClient(context)

        // Send the data
        dataClient.putDataItem(putDataRequest)
            .addOnSuccessListener {
                Log.d("WearOS", "${action.name} clicked.")
            }
            .addOnFailureListener { exception ->
                Log.e("WearOS", "Failed to send data.", exception)
            }
    }
}