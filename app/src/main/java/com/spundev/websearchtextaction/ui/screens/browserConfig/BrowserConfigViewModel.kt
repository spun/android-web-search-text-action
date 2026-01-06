package com.spundev.websearchtextaction.ui.screens.browserConfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spundev.websearchtextaction.data.SearchMode
import com.spundev.websearchtextaction.data.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserConfigViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<BrowserConfigUiState> =
        userPreferencesRepository.browserModeConfigFlow.map {
            BrowserConfigUiState.Success(
                selectedSearchURL = it.selectedSearchUrl,
                customSearchURL = it.customSearchUrl
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BrowserConfigUiState.Loading
        )

    fun setSelectedSearchUrl(url: String) {
        viewModelScope.launch {
            launch {
                userPreferencesRepository.setSelectedSearchUrl(url)
            }
            // If browser config is modified, assume that browser mode should be enabled
            launch {
                userPreferencesRepository.setSearchMode(SearchMode.BROWSER)
            }
        }
    }

    fun setCustomSearchUrl(url: String) {
        viewModelScope.launch {
            // Store new custom URL
            launch {
                userPreferencesRepository.setCustomSearchUrl(url)
            }
            // Set the new custom URL as the active one
            launch {
                userPreferencesRepository.setSelectedSearchUrl(url)
            }
            // If browser config is modified, assume that browser mode should be enabled
            launch {
                userPreferencesRepository.setSearchMode(SearchMode.BROWSER)
            }
        }
    }
}

sealed interface BrowserConfigUiState {
    data object Loading : BrowserConfigUiState
    data class Success(
        val selectedSearchURL: String,
        val customSearchURL: String,
    ) : BrowserConfigUiState
}