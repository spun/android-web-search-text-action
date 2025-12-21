package com.spundev.websearchtextaction

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class ProcessTextActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        if (!text.isNullOrEmpty()) {

            // Option 1: Use SEARCH app
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, text)
            startActivity(intent)

            // Option 2: Use ACTION_VIEW with a URL to use the browser
            // val encodedText = URLEncoder.encode(text.toString(), StandardCharsets.UTF_8.toString())
            // val searchUrl = "https://<selected_search_provider>/?q=%s".format(encodedText)
            // val intent = Intent(Intent.ACTION_VIEW, searchUrl.toUri())

            try {
                startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(
                    this,
                    "No app found to perform the search.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
