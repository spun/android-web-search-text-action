package com.spundev.websearchtextaction.ui.screens.browserConfig

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spundev.websearchtextaction.R

private data class SearchProvider(
    val id: String,
    val name: String,
    val url: String,
)

private val listSearchProviders = listOf<SearchProvider>(
    SearchProvider(id = "g", name = "Google", url = "google.com"),
    SearchProvider(id = "ddg", name = "DuckDuckGo", url = "duckduckgo.com"),
    SearchProvider(id = "b", name = "Microsoft Bing", url = "bing.com"),
)

@Composable
fun BrowserConfigRoute(
    onBack: () -> Unit,
    viewModel: BrowserConfigViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BrowserConfigScreen(
        uiState = uiState,
        onBack = onBack,
        onProviderSelected = viewModel::setSearchProvider,
        modifier = Modifier.safeContentPadding()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserConfigScreen(
    uiState: BrowserConfigUiState,
    onBack: () -> Unit,
    onProviderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = { Text("Search provider") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back_24),
                        contentDescription = "Navigate up",
                    )
                }
            }
        )
        when (uiState) {
            BrowserConfigUiState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            }

            is BrowserConfigUiState.Success -> {
                val selected = uiState.searchProvider
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    listSearchProviders.forEach {
                        ListItem(
                            colors = ListItemDefaults.colors().copy(
                                containerColor = Color.Transparent
                            ),
                            headlineContent = { Text(it.name) },
                            supportingContent = { Text(it.url) },
                            trailingContent = {
                                RadioButton(selected = it.id == selected, onClick = null)
                            },
                            modifier = Modifier.clickable(onClick = { onProviderSelected(it.id) })
                        )
                    }
                }
            }
        }
    }
}