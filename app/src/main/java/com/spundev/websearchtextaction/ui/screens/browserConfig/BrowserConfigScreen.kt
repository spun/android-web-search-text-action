package com.spundev.websearchtextaction.ui.screens.browserConfig

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun BrowserConfigRoute(
    viewModel: BrowserConfigViewModel = hiltViewModel()
) {
    Text("TODO: ${viewModel.from}")
}