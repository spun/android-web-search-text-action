package com.spundev.websearchtextaction.model

data class SearchProvider(
    val id: String,
    val name: String,
    val url: String,
    val searchUrl: String,
)

val DEFAULT_SEARCH_PROVIDER = SearchProvider(
    id = "g",
    name = "Google",
    url = "google.com",
    searchUrl = "https://www.google.com/search?q=%s"
)

val listSearchProviders: List<SearchProvider> = listOf(
    DEFAULT_SEARCH_PROVIDER,
    SearchProvider(
        id = "mb",
        name = "Microsoft Bing",
        url = "bing.com",
        searchUrl = "https://www.bing.com/search?q=%s"
    ),
    SearchProvider(
        id = "ys",
        name = "Yahoo",
        url = "yahoo.com",
        searchUrl = "https://search.yahoo.com/search?p=%s"
    ),
    SearchProvider(
        id = "ddg",
        name = "DuckDuckGo",
        url = "duckduckgo.com",
        searchUrl = "https://duckduckgo.com/?q=%s"
    ),
    SearchProvider(
        id = "ec",
        name = "Ecosia",
        url = "ecosia.org",
        searchUrl = "https://www.ecosia.org/search?q=%s"
    ),
    SearchProvider(
        id = "qw",
        name = "Qwant",
        url = "qwant.com",
        searchUrl = "https://www.qwant.com/?q=%s"
    ),
    SearchProvider(
        id = "bs",
        name = "Brave",
        url = "search.brave.com",
        searchUrl = "https://search.brave.com/search?q=%s"
    ),
    SearchProvider(
        id = "pw",
        name = "PrivacyWall",
        url = "privacywall.org",
        searchUrl = "https://www.privacywall.org/search/secure/?q=%s"
    ),
)