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

    val searchModeFlow: Flow<SearchMode> = userPreferencesFlow.map {
        when (it.modeCase) {
            UserPreferences.ModeCase.SEARCH_APP -> SearchMode.SearchApp
            UserPreferences.ModeCase.BROWSER_URL -> SearchMode.BrowserUrl(it.browserUrl.url)
            UserPreferences.ModeCase.MODE_NOT_SET -> {
                // If nothing is set, use SearchApp
                SearchMode.SearchApp
            }
        }
    }

    suspend fun getSearchMode(): SearchMode {
        val up = userPreferencesFlow.first()
        return when (up.modeCase) {
            UserPreferences.ModeCase.SEARCH_APP -> SearchMode.SearchApp
            UserPreferences.ModeCase.BROWSER_URL -> SearchMode.BrowserUrl(up.browserUrl.url)
            UserPreferences.ModeCase.MODE_NOT_SET -> {
                // If nothing is set, use SearchApp
                SearchMode.SearchApp
            }
        }
    }

    suspend fun setSearchMode(searchMode: SearchMode) {
        userPreferencesStore.updateData { currentPreferences ->
            currentPreferences.toBuilder().apply {
                when (searchMode) {
                    SearchMode.SearchApp -> searchApp = SearchApp.getDefaultInstance()
                    is SearchMode.BrowserUrl -> browserUrl =
                        BrowserURL.newBuilder().setUrl(searchMode.url).build()
                }
            }.build()
        }
    }
}

sealed class SearchMode {
    data object SearchApp : SearchMode()
    data class BrowserUrl(val url: String) : SearchMode()
}
