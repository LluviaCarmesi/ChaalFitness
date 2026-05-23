package com.example.chaalfitness

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. Create a singleton instance of DataStore attached to the app Context
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_information")

class DeviceStoredValues(private val context: Context) {

    companion object {
        // 2. Define unique keys for our database rows
        val CURRENT_GOAL_KEY = stringPreferencesKey("current_goal")
        val CURRENT_WEIGHT_KEY = stringPreferencesKey("current_weight")
        val CURRENT_HEIGHT_KEY = stringPreferencesKey("height")
        val CURRENT_AGE_KEY = stringPreferencesKey("age") // TODO: Change to DOB
    }

    // 3. READ DATA: Expose values as asynchronous streams (Flows)
    val currentGoalFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_GOAL_KEY] ?: "" // Return empty string if no data is saved yet
    }
    val currentWeightFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_WEIGHT_KEY] ?: "" // Return empty string if no data is saved yet
    }

    val heightFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_HEIGHT_KEY] ?: ""
    }

    val ageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_AGE_KEY] ?: ""
    }

    // 4. WRITE DATA: Suspend functions ensure file writing happens off the main UI thread
    suspend fun saveMetrics(currentGoal: String, currentWeight: String, currentHeight: String, currentAge: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_GOAL_KEY] = currentGoal
            preferences[CURRENT_WEIGHT_KEY] = currentWeight
            preferences[CURRENT_HEIGHT_KEY] = currentHeight
            preferences[CURRENT_AGE_KEY] = currentAge
        }
    }
}