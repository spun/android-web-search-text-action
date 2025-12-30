package com.spundev.websearchtextaction.ui.screens.browserConfig

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserConfigViewModel @Inject constructor() : ViewModel() {

    // TODO: Use DataStore
    private val selectedSearchProvider = MutableStateFlow("g")

    val uiState = selectedSearchProvider.map {
        BrowserConfigUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BrowserConfigUiState.Loading
    )

    fun setSearchProvider(searchProvider: String) {
        viewModelScope.launch {
            selectedSearchProvider.value = searchProvider
        }
    }
}

sealed interface BrowserConfigUiState {
    data object Loading : BrowserConfigUiState
    data class Success(
        val searchProvider: String,
    ) : BrowserConfigUiState
}