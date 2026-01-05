package com.spundev.websearchtextaction.ui.screens.browserConfig

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spundev.websearchtextaction.R
import com.spundev.websearchtextaction.model.listSearchProviders
import com.spundev.websearchtextaction.util.isValidUrl
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BrowserConfigRoute(
    onBack: () -> Unit,
    viewModel: BrowserConfigViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BrowserConfigScreen(
        uiState = uiState,
        onBack = onBack,
        onSelectedSearchURLChange = viewModel::setSelectedSearchUrl,
        onCustomSearchUrlChange = viewModel::setCustomSearchUrl,
        modifier = Modifier.windowInsetsPadding(
            insets = WindowInsets.safeDrawing.only(
                sides = WindowInsetsSides.Horizontal
            )
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BrowserConfigScreen(
    uiState: BrowserConfigUiState,
    onBack: () -> Unit,
    onSelectedSearchURLChange: (String) -> Unit,
    onCustomSearchUrlChange: (String) -> Unit,
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
                val selectedURL = uiState.selectedSearchURL
                val customURL = uiState.customSearchURL
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Custom option is always first
                    CustomOptionListItem(
                        initialURL = customURL,
                        isSelected = selectedURL == customURL || listSearchProviders.none { it.searchUrl == selectedURL },
                        onCustomURLChange = onCustomSearchUrlChange
                    )
                    // Display predefined search providers
                    listSearchProviders.forEach {
                        ListItem(
                            colors = ListItemDefaults.colors().copy(
                                containerColor = Color.Transparent
                            ),
                            headlineContent = { Text(it.name) },
                            supportingContent = { Text(it.url) },
                            trailingContent = {
                                RadioButton(selected = it.searchUrl == selectedURL, onClick = null)
                            },
                            modifier = Modifier.clickable {
                                onSelectedSearchURLChange(it.searchUrl)
                            }
                        )
                    }

                    // Bottom system bar
                    Spacer(
                        modifier = Modifier.windowInsetsBottomHeight(
                            insets = WindowInsets.systemBars
                        )
                    )
                }
            }
        }
    }
}

private fun getCustomOptionErrorMessage(text: String): String? {
    return if (text.isEmpty()) {
        "Please enter a URL"
    } else if (!isValidUrl(text)) {
        "That URL doesn’t look right. Please check it and try again"
    } else if (!text.contains("%s")) {
        "Try including %s in place of the search term"
    } else {
        null
    }
}

@Composable
private fun CustomOptionListItem(
    initialURL: String,
    isSelected: Boolean,
    onCustomURLChange: (String) -> Unit,
) {
    var openDialog by remember { mutableStateOf(false) }

    ListItem(
        colors = ListItemDefaults.colors().copy(containerColor = Color.Transparent),
        headlineContent = { Text("Custom") },
        supportingContent = { Text(text = if (isSelected && !initialURL.isEmpty()) initialURL else "Custom search url") },
        trailingContent = { RadioButton(selected = isSelected, onClick = null) },
        modifier = Modifier.clickable(onClick = { openDialog = true })
    )

    if (openDialog) {
        val state = rememberTextFieldState(initialURL)
        var inputError by remember { mutableStateOf<String?>(null) }
        var checkErrorsOnEdit by remember { mutableStateOf(false) }

        // After first error is displayed, keep checking after each edit
        if (checkErrorsOnEdit) {
            LaunchedEffect(state) {
                snapshotFlow { state.text.toString() }.collectLatest { text ->
                    inputError = getCustomOptionErrorMessage(text)
                }
            }
        }

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog = false
            },
            title = { Text(text = "Custom search url") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = "URL with %s in place of search term.\n\nFor example:\nhttps://www.google.com/search?q=%s")
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        state = state,
                        isError = inputError != null,
                        supportingText = { inputError?.let { Text(it) } }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val text = state.text.toString()
                        val errorMsg = getCustomOptionErrorMessage(text)
                        if (errorMsg != null) {
                            inputError = errorMsg
                            checkErrorsOnEdit = true
                        } else {
                            onCustomURLChange(text)
                            openDialog = false
                        }
                    }
                ) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { openDialog = false }) { Text("Cancel") }
            },
        )
    }
}
