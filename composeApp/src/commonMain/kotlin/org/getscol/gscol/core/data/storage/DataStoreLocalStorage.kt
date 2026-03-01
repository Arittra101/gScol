package org.getscol.gscol.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.getscol.gscol.core.utils.AppLogger

class DataStoreLocalStorage(
    private val dataStore: DataStore<Preferences>
) : LocalStorage {

    // String operations
    override suspend fun setString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
        AppLogger.d("String set: $key = $value")
    }

    override suspend fun getString(key: String): String? {
        AppLogger.d("String get: $key")
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)]
        }.first()
    }

    // Int operations
    override suspend fun setInt(key: String, value: Int) {
        dataStore.edit { preferences ->
            preferences[intPreferencesKey(key)] = value
        }
        AppLogger.d("Int set: $key = $value")
    }

    override suspend fun getInt(key: String): Int? {
        AppLogger.d("Int get: $key")
        return dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)]
        }.first()
    }

    // Boolean operations
    override suspend fun setBoolean(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
        AppLogger.d("Boolean set: $key = $value")
    }

    override suspend fun getBoolean(key: String): Boolean? {
        AppLogger.d("Boolean get: $key")
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)]
        }.first()
    }

    override fun getFlowBoolean(key: String): Flow<Boolean> {
        AppLogger.d("Boolean get: $key")
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: false
        }
    }

    override fun getFlowInt(key: String): Flow<Int> {
        AppLogger.d("Int get: $key")
        return dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)] ?: 0
        }
    }

    // Long operations
    override suspend fun setLong(key: String, value: Long) {
        dataStore.edit { preferences ->
            preferences[longPreferencesKey(key)] = value
        }
        AppLogger.d("Long set: $key = $value")
    }

    override suspend fun getLong(key: String): Long? {
        AppLogger.d("Long get: $key")
        return dataStore.data.map { preferences ->
            preferences[longPreferencesKey(key)]
        }.first()
    }

    // Float operations
    override suspend fun setFloat(key: String, value: Float) {
        dataStore.edit { preferences ->
            preferences[floatPreferencesKey(key)] = value
        }
        AppLogger.d("Float set: $key = $value")
    }

    override suspend fun getFloat(key: String): Float? {
        AppLogger.d("Float get: $key")
        return dataStore.data.map { preferences ->
            preferences[floatPreferencesKey(key)]
        }.first()
    }

    // General operations
    override suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            // Try removing all possible types for this key
            preferences.remove(stringPreferencesKey(key))
            preferences.remove(intPreferencesKey(key))
            preferences.remove(booleanPreferencesKey(key))
            preferences.remove(longPreferencesKey(key))
            preferences.remove(floatPreferencesKey(key))
        }
        AppLogger.d("Removed from DataStore: $key")
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        AppLogger.d("Cleared DataStore")
    }

    override fun getAll(): Flow<Map<String, Any>> {
        AppLogger.d("Getting all from DataStore")
        return dataStore.data.map { preferences ->
            preferences.asMap().mapKeys { it.key.name }
        }
    }
}