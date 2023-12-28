package com.jlpc.facetimelapsemaker.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class PreferenceManager(context: Context) {

    companion object {
        private val FORMAT_KEY = stringPreferencesKey("format_preference_key")
        private val FPS_KEY = intPreferencesKey("fps_preference_key")
        private val QUALITY_KEY = stringPreferencesKey("quality_preference_key")
    }

    private val dataStore = context.dataStore

    suspend fun saveFormat(format: String) {
        dataStore.edit { preferences ->
            preferences[FORMAT_KEY] = format
        }
    }
}
