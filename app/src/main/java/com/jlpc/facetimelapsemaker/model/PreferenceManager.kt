package com.jlpc.facetimelapsemaker.model

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class PreferenceManager(context: Context) {

    companion object {
        private val FORMAT_KEY = stringPreferencesKey("format_preference_key")
        private val FPS_KEY = intPreferencesKey("fps_preference_key")
        private val QUALITY_KEY = stringPreferencesKey("quality_preference_key")
    }

    private val dataStore = context.dataStore
    private val TAG = "PreferenceManager"

    suspend fun saveFormat(format: String) {
        dataStore.edit { preferences ->
            preferences[FORMAT_KEY] = format
        }
    }

    suspend fun saveFPS(fps: Int) {
        dataStore.edit { preferences ->
            preferences[FPS_KEY] = fps
        }
    }

    suspend fun saveQuality(quality: String) {
        dataStore.edit { preferences ->
            preferences[QUALITY_KEY] = quality
        }
    }

    val formatFlow: Flow<String?> = dataStore.data.catch {
            exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
            preferences ->
        preferences[FORMAT_KEY]
    }

    val qualityFlow: Flow<String?> = dataStore.data.catch {
            exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
            preferences ->
        preferences[QUALITY_KEY]
    }

    val fpsFlow: Flow<Int?> = dataStore.data.catch {
            exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading preferences: $exception")
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {
            preferences ->
        preferences[FPS_KEY]
    }
}
