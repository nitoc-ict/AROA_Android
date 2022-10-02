package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.select_ble_device

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.local.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@SuppressLint("MissingPermission") // IDEのバグで、Manifestにパーミッションを記述してもエラーが出る。
class SelectBleDeviceViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    private val bluetoothManager =
        application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    private val _boundedDeviceList = MutableStateFlow<List<BluetoothDevice>>(listOf())
    val boundedDeviceList: StateFlow<List<BluetoothDevice>> = _boundedDeviceList

    fun fetchBoundedDeviceList() {
        viewModelScope.launch {
            _boundedDeviceList.emit(
                bluetoothAdapter.bondedDevices.toList()
            )
        }
    }

    fun saveSelectedDeviceAddress(address: String) = runBlocking {
        userPreferences.saveSelectedDeviceAddress(address)
    }
}