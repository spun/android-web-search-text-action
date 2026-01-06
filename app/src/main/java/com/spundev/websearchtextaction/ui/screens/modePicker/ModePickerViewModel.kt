package com.spundev.websearchtextaction.ui.screens.modePicker

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
class ModePickerViewModel @Inject constructor(
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<ModePickerUiState> = userPreferencesRepository.searchModeFlow.map {
        ModePickerUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ModePickerUiState.Loading
    )

    fun setSearchMode(searchMode: SearchMode) {
        viewModelScope.launch {
            userPreferencesRepository.setSearchMode(searchMode)
        }
    }
}

sealed interface ModePickerUiState {
    data object Loading : ModePickerUiState
    data class Success(
        val selectedSearchMode: SearchMode,
    ) : ModePickerUiState
}
