package jp.ac.okinawa_ct.nitoc_ict.aroa.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences(context: Context) {
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val SELECTED_DEVICE_ADDRESS = stringPreferencesKey("paired_device_address")
        private val IS_USE_WEAR_OS = booleanPreferencesKey("is_use_wear_os")
        private val IS_USE_NREAL_AIR = booleanPreferencesKey("is_use_nreal_air")
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    val userId: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[USER_ID]
        }

    val selectedDeviceAddress: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[SELECTED_DEVICE_ADDRESS]
        }

    val isUseWearOs: Flow<Boolean>
        get() = dataStore.data.map { preference ->
            preference[IS_USE_WEAR_OS] ?: false
        }

    val isUseNrealAir: Flow<Boolean>
        get() = dataStore.data.map { preference ->
            preference[IS_USE_NREAL_AIR] ?: false
        }

    suspend fun savaUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun saveSelectedDeviceAddress(deviceAddress: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_DEVICE_ADDRESS] = deviceAddress
        }
    }

    suspend fun saveIsUseWearOs(isUse: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_USE_WEAR_OS] = isUse
        }
    }

    suspend fun saveIsUseNrealAir(isUse: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_USE_NREAL_AIR] = isUse
        }
    }
}