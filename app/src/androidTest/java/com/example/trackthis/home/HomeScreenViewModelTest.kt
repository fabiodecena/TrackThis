package com.example.trackthis.home

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trackthis.R
import com.example.trackthis.data.TopicListElement
import com.example.trackthis.data.TopicListRepository
import com.example.trackthis.data.listOfVisualizedTopicListItem
import com.example.trackthis.ui.home.HomeScreenViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * [HomeScreenViewModelTest] is a class that tests the [HomeScreenViewModel] class.
 *
 * This test class verifies the behavior of the HomeScreenViewModel, including:
 * - Updating the selected state of a topic and persisting it.
 * - Toggling the expanded state of a topic.
 * - Collapsing a topic when it is already expanded.
 *
 * It uses JUnit and AndroidX Test libraries to perform the tests.
 */
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
    /**
     * This test verifies that when a topic is selected, the following happens:
     * - The selected state of the topic is updated in the ViewModel.
     * - The updated topic list is emitted by the ViewModel's `homeScreenUiState` flow.
     * - The `selectedTopics` property of the `TopicListRepository` is updated with the selected topic.
     */
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
    /**
     * This test verifies that when `toggleExpanded` is called with a topic name,
     * the `expandedTopicName` property of the `HomeScreenUiState` is updated
     * to the provided topic name.
     */
    @Test
    fun toggleExpanded_updates_expandedTopicName() = runTest {
        val topicName = 1

        viewModel.toggleExpanded(topicName)

        val uiState = viewModel.homeScreenUiState.first()
        assertEquals(topicName, uiState.expandedTopicName)
    }
    /**
     * This test verifies that when `toggleExpanded` is called with a topic name
     * that is already expanded, the `expandedTopicName` property of the
     * `HomeScreenUiState` is updated to 0, indicating that the topic is now
     * collapsed.
     */
    @Test
    fun toggleExpanded_collapses_when_already_expanded() = runTest {
        val topicName = 1

        viewModel.toggleExpanded(topicName) // Expand
        viewModel.toggleExpanded(topicName) // Collapse

        val uiState = viewModel.homeScreenUiState.first()
        assertEquals(0, uiState.expandedTopicName)
    }
}