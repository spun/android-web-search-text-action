package com.spundev.websearchtextaction.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.spundev.websearchtextaction.model.DEFAULT_SEARCH_PROVIDER
import java.io.InputStream
import java.io.OutputStream

internal object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences =
        UserPreferences.getDefaultInstance().toBuilder()
            .setBrowserConfig(
                BrowserModeConfig.getDefaultInstance().toBuilder()
                    .setSelectedSearchUrl(DEFAULT_SEARCH_PROVIDER.searchUrl)
                    .build()
            ).build()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}
