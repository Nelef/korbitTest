package com.uyjang.korbittest.data.internal

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteDataSource(private val context: Context) {
    private val Context.preferenceDataStore by preferencesDataStore(name = "favoriteDataStore")
    private val key = stringPreferencesKey("favorite")

    fun getPreference(): Flow<List<String>> {
        return context.preferenceDataStore.data.map { preferences ->
            val json = preferences[key] ?: ""
            if (json.isBlank()) {
                emptyList()
            } else {
                try {
                    Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
                } catch (e: JsonSyntaxException) {
                    emptyList()
                }
            }
        }
    }


    suspend fun setPreference(list: List<String>) {
        val json = Gson().toJson(list)
        context.preferenceDataStore.edit { preferences ->
            preferences[key] = json
        }
    }
}
