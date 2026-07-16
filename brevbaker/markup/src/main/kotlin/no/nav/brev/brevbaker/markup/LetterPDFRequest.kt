package no.nav.brev.brevbaker.markup

import kotlinx.serialization.Serializable

/** Språket brevet er skrevet på. Styrer bl.a. datoformatering og språkoppsett i pdf-bygger. */
enum class LanguageCode {
    BOKMAL,
    NYNORSK,
    ENGLISH,
}

/** Hva slags brev markup-en representerer. */
enum class Brevtype {
    VEDTAKSBREV,
    INFORMASJONSBREV,
}

/**
 * En ferdig bestilling som pdf-bygger kan rendre til PDF: hovedbrevet, eventuelle brevvedlegg og
 * frittstående PDF-vedlegg-titler, sammen med språk og brevtype.
 *
 * Bygges via markup-DSL-en og serialiseres med [toJson].
 */
@ConsistentCopyVisibility
@Serializable
data class LetterPDFRequest internal constructor(
    val letterMarkup: LetterMarkup,
    val attachments: List<Attachment>,
    val pdfVedlegg: List<PDFTittel>,
    val language: LanguageCode,
    val brevtype: Brevtype,
)

/** Serialiser et [LetterPDFRequest] til JSON. Bruk dette når du bestiller en PDF fra pdf-bygger. */
fun LetterPDFRequest.toJson(): String =
    markupJson.encodeToString(LetterPDFRequest.serializer(), this)

/** Deserialiser et [LetterPDFRequest] fra JSON produsert av [toJson]. */
fun decodeLetterPDFRequest(json: String): LetterPDFRequest =
    markupJson.decodeFromString(LetterPDFRequest.serializer(), json)
