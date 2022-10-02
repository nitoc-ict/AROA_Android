package jp.ac.okinawa_ct.nitoc_ict.aroa.ui.config_join_trial

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import jp.ac.okinawa_ct.nitoc_ict.aroa.data.local.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

@SuppressLint("MissingPermission") // IDEのバグで、Manifestにパーミッションを記述してもエラーが出る。
class ConfigJoinTrialViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    private val bluetoothManager =
        application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    val isUseNrealAir = userPreferences.isUseNrealAir
    val isUseWearOs = userPreferences.isUseWearOs

    private val selectedDeviceAddress = userPreferences.selectedDeviceAddress

    val selectedDeviceName: Flow<String?> = selectedDeviceAddress.map { address ->
        return@map try {
            bluetoothAdapter.bondedDevices.find {
                it.address == address
            }?.name
        } catch (e: SecurityException) {
            null
        }
    }

    fun saveIsUseWearOs(isUse: Boolean) = runBlocking {
        userPreferences.saveIsUseWearOs(isUse)
    }

    fun saveIsUseNrealAir(isUse: Boolean) = runBlocking {
        userPreferences.saveIsUseNrealAir(isUse)
    }
}