package com.example.trackthis.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Extension property to access the DataStore instance for the application's preferences.
 *
 * This provides a convenient way to access the [DataStore], which is used for storing
 * key-value pairs of application preferences. The DataStore is named "topic_preferences".
 */
private val Context.dataStore by preferencesDataStore("topic_preferences")
/**
 *  Defines the keys used to store and retrieve topic preferences in the DataStore.
 */
object TopicPreferences {
    /**
     * Key for storing the set of selected topics.
     *
     * This key is used to access the set of strings representing the topics
     * that the user has selected.
     */
    val SELECTED_TOPICS_KEY = stringSetPreferencesKey("selected_topics")
}

/**
 *  Repository class for managing the user's selected topics.
 *
 *  This class provides methods to save and retrieve the set of selected topics
 *  using the [DataStore]. It encapsulates the data access logic for topic preferences.
 *
 *  @param context The application context used to access the DataStore.
 */
class TopicListRepository(context: Context) {
    private val dataStore = context.dataStore
    /**
     * Saves the set of selected topics to the DataStore.
     *
     * @param selectedTopics The set of topic strings to be saved.
     */
    suspend fun saveSelectedTopics(selectedTopics: Set<String>) {
        dataStore.edit { preferences ->
            preferences[TopicPreferences.SELECTED_TOPICS_KEY] = selectedTopics
        }
    }
    /**
     * Retrieves the flow of selected topics from the DataStore.
     *
     * This returns a [Flow] that emits the current set of selected topics whenever
     * the data in the DataStore changes. If no topics have been selected, it emits an empty set.
     *
     * @return A [Flow] emitting the set of selected topics.
     */
    val selectedTopics: Flow<Set<String>> = dataStore.data.map { preferences ->
        preferences[TopicPreferences.SELECTED_TOPICS_KEY] ?: retrieveSelectedTopics()
    }
    private var defaultSelectedTopics = mutableSetOf<String>()
    /**
     * Retrieves the default set of selected topics.
     *
     * This function iterates through a [listOfVisualizedTopicListItem] and adds the name of the topic
     * to the set if the [TopicListElement] is selected.
     *
     * @return The set of default selected topics.
     */
    private fun retrieveSelectedTopics(): Set<String> {
        listOfVisualizedTopicListItem.forEach {
            if (it.selected) {
                defaultSelectedTopics.add(it.name.toString())
            }
        }
        return defaultSelectedTopics
    }
}