package com.spundev.websearchtextaction.ui.screens.browserConfig

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BrowserConfigViewModel @Inject constructor() : ViewModel() {
    val from = "From viewModel"

    override fun onCleared() {
        Timber.d("BrowserConfig cleared")
        super.onCleared()
    }
}