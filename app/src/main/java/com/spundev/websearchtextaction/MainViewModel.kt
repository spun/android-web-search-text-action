package com.spundev.websearchtextaction

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
class MainViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = userPreferencesRepository.searchModeFlow.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityUiState.Loading
    )

    fun setSearchMode(searchMode: SearchMode) {
        viewModelScope.launch {
            userPreferencesRepository.setSearchMode(searchMode)
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(
        val selectedSearchMode: SearchMode,
    ) : MainActivityUiState
}