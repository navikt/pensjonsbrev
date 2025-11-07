package no.nav.pensjon.brev.skribenten.serialize

import io.ktor.http.ContentType
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutgoingContent
import io.ktor.http.withCharset
import io.ktor.serialization.ContentConverter
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import no.nav.pensjon.brev.skribenten.model.Api

object PdfResponseConverter : ContentConverter {
    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? = null

    override suspend fun serialize(contentType: ContentType, charset: Charset, typeInfo: TypeInfo, value: Any?): OutgoingContent? {
        return if (value is Api.PdfResponse && contentType == ContentType.Application.Pdf) {
            ByteArrayContent(value.pdf, ContentType.Application.Pdf)
        } else null
    }
}