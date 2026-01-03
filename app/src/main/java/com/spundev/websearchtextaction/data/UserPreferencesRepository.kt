package com.spundev.websearchtextaction.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesStore: DataStore<UserPreferences>
) {

    private val userPreferencesFlow: Flow<UserPreferences> =
        userPreferencesStore.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Timber.e(exception, "Error reading appearance preferences.")
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    val searchModeFlow: Flow<SearchMode> = userPreferencesFlow.map { it.searchMode }

    suspend fun getSearchMode(): SearchMode = userPreferencesFlow.first().searchMode

    suspend fun setSearchMode(searchMode: SearchMode) {
        userPreferencesStore.updateData { currentPreferences ->
            currentPreferences.toBuilder().setSearchMode(searchMode).build()
        }
    }

    val browserModeConfigFlow: Flow<BrowserModeConfig> =
        userPreferencesFlow.map { it.browserConfig }

    suspend fun getBrowserModeConfig(): BrowserModeConfig =
        userPreferencesFlow.first().browserConfig

    suspend fun setSelectedSearchUrl(url: String) {
        userPreferencesStore.updateData { currentPreferences ->
            val browserConfig =
                currentPreferences.browserConfig.toBuilder().setSelectedSearchUrl(url).build()
            currentPreferences.toBuilder().setBrowserConfig(browserConfig).build()
        }
    }

    suspend fun setCustomSearchUrl(url: String) {
        userPreferencesStore.updateData { currentPreferences ->
            val browserConfig =
                currentPreferences.browserConfig.toBuilder().setCustomSearchUrl(url).build()
            currentPreferences.toBuilder().setBrowserConfig(browserConfig).build()
        }
    }
}
