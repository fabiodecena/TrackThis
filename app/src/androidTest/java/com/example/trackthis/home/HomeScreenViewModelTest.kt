package com.example.trackthis.home

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.R
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.listOfVisualizedTopicListItem
import com.example.trackthis.ui.home.HomeScreenUiState
import com.example.trackthis.ui.home.HomeScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class HomeScreenViewModelTest {

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var topicListRepository: TopicListRepository
    private lateinit var context: Context

    private var sampleTopics = mutableListOf(
        TopicListElement(R.string.architecture, R.drawable.architecture, false),
        TopicListElement(R.string.design, R.drawable.design, true)
    )

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        topicListRepository = TopicListRepository(context)
        viewModel = HomeScreenViewModel(topicListRepository)
    }

    @Test
    fun onTopicSelected_updates_selected_state_and_persist() = runTest {
        /**
         * Load the sample topics into the repository
         */
        val topicToSelect = sampleTopics[0]

        viewModel.onTopicSelected(topicToSelect)
        /**
         * Updated Topics should be not an empty List and should update the selected state.
         * In this case, the first topic of the sample list should be selected, also if starting with a false value
         */
        val updatedTopics = listOfVisualizedTopicListItem
        assertEquals(true, updatedTopics.find { it.name == sampleTopics[0].name }?.selected)

        // Verify that the repository's selectedTopics is called with the correct argument
        assertEquals((sampleTopics[0].name), topicListRepository.selectedTopics.first().first().toInt())
    }

    @Test
    fun toggleExpanded_updates_expandedTopicName() = runTest {
        val topicName = 1

        viewModel.toggleExpanded(topicName)

        val uiState = viewModel.homeScreenUiState.first()
        assertEquals(topicName, uiState.expandedTopicName)
    }

    @Test
    fun toggleExpanded_collapses_when_already_expanded() = runTest {
        val topicName = 1

        viewModel.toggleExpanded(topicName) // Expand
        viewModel.toggleExpanded(topicName) // Collapse

        val uiState = viewModel.homeScreenUiState.first()
        assertEquals(0, uiState.expandedTopicName)
    }
}