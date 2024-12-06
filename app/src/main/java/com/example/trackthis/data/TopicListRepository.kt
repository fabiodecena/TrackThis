package com.example.trackthis.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.map



private val Context.dataStore by preferencesDataStore("topic_preferences")

object TopicPreferences {
    val SELECTED_TOPICS_KEY = stringSetPreferencesKey("selected_topics")
}

class TopicListRepository(private val context: Context) {
    private val dataStore = context.dataStore

    // Save the selected topics
    suspend fun saveSelectedTopics(selectedTopics: Set<String>) {
        dataStore.edit { preferences ->
            preferences[TopicPreferences.SELECTED_TOPICS_KEY] = selectedTopics
        }
    }

    // Retrieve the selected topics
    val selectedTopics: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[TopicPreferences.SELECTED_TOPICS_KEY] ?: emptySet()
    }
}