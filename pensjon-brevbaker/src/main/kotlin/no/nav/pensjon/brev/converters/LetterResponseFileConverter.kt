package no.nav.pensjon.brev.converters

import io.ktor.content.ByteArrayContent
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.charsets.*
import no.nav.pensjon.brev.api.model.LetterResponse

object LetterResponseFileConverter : ContentConverter {
    override suspend fun deserialize(
        charset: Charset,
        typeInfo: TypeInfo,
        content: ByteReadChannel,
    ): Any? = null

    override suspend fun serialize(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?,
    ): OutgoingContent? {
        return if (value is LetterResponse) {
            ContentType.parse(value.contentType)
                .takeIf { contentType.withCharset(charset).match(it) }
                ?.let { ByteArrayContent(value.file, it) }
        } else {
            null
        }
    }
}
