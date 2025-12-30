package com.spundev.websearchtextaction.ui.screens.modePicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spundev.websearchtextaction.R
import com.spundev.websearchtextaction.data.SearchMode

@Composable
fun ModePickerRoute(
    onBrowserConfig: () -> Unit,
    viewModel: ModePickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ModePickerScreen(
        uiState = uiState,
        onSearchModeChange = viewModel::setSearchMode,
        onBrowserConfig = onBrowserConfig,
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding()
    )
}

@Composable
private fun ModePickerScreen(
    uiState: ModePickerUiState,
    onSearchModeChange: (SearchMode) -> Unit,
    onBrowserConfig: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        when (uiState) {
            ModePickerUiState.Loading -> {
                CircularProgressIndicator()
            }

            is ModePickerUiState.Success -> {
                SearchModeButtons(
                    selectedSearchMode = uiState.selectedSearchMode,
                    onSearchModeChange = onSearchModeChange,
                    onBrowserConfig = onBrowserConfig
                )
            }
        }
    }
}

@Composable
private fun SearchModeButtons(
    selectedSearchMode: SearchMode,
    onSearchModeChange: (SearchMode) -> Unit,
    onBrowserConfig: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        SearchAppButton(
            isSelected = selectedSearchMode == SearchMode.SearchApp,
            onClick = { onSearchModeChange(SearchMode.SearchApp) }
        )
        BrowserModeButton(
            isSelected = selectedSearchMode is SearchMode.BrowserUrl,
            onClick = { onSearchModeChange(SearchMode.BrowserUrl("demo")) },
            onConfig = onBrowserConfig
        )
    }
}

private val ButtonBigRadius = 16.dp
private val ButtonSmallRadius = 4.dp

@Composable
private fun SearchAppButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = ButtonBigRadius,
                    topEnd = ButtonBigRadius,
                    bottomEnd = ButtonSmallRadius,
                    bottomStart = ButtonSmallRadius
                )
            )
            .background(MaterialTheme.colorScheme.surfaceBright)
            .height(72.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(text = "Search App")
    }
}

@Composable
private fun BrowserModeButton(
    isSelected: Boolean,
    onClick: () -> Unit,
    onConfig: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = ButtonSmallRadius,
                    topEnd = ButtonSmallRadius,
                    bottomEnd = ButtonBigRadius,
                    bottomStart = ButtonBigRadius
                )
            )
            .background(MaterialTheme.colorScheme.surfaceBright)
            .height(72.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = onClick)
                .padding(16.dp)
        ) {
            RadioButton(selected = isSelected, onClick = null)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Browser")
            Spacer(
                modifier = Modifier
                    .widthIn(min = 16.dp)
            )
        }
        VerticalDivider(modifier = Modifier.heightIn(max = 32.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .clickable(onClick = onConfig)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings_24),
                contentDescription = "Browser mode settings",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
