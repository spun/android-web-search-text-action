package com.spundev.websearchtextaction

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.spundev.websearchtextaction.data.SearchMode
import com.spundev.websearchtextaction.data.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@AndroidEntryPoint
class ProcessTextActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val selectedSearchMode = userPreferencesRepository.getSearchMode()

            Timber.d("Selected mode is: $selectedSearchMode")

            val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
            if (!text.isNullOrEmpty()) {

                val intent = when (selectedSearchMode) {
                    SearchMode.SearchApp -> {
                        // Option 1: Use SEARCH app
                        val intent = Intent(Intent.ACTION_WEB_SEARCH)
                        intent.putExtra(SearchManager.QUERY, text)
                        intent
                    }

                    is SearchMode.BrowserUrl -> {
                        // Option 2: Use ACTION_VIEW with a URL to use the browser
                        val encodedText =
                            URLEncoder.encode(text.toString(), StandardCharsets.UTF_8.toString())
                        val searchUrl =
                            "https://<selected_search_provider>/?q=%s&kl=es-es&kbj=1".format(
                                encodedText
                            )
                        Intent(Intent.ACTION_VIEW, searchUrl.toUri())
                    }
                }

                try {
                    startActivity(intent)
                } catch (_: Exception) {
                    Toast.makeText(
                        this@ProcessTextActivity,
                        "No app found to perform the search.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
