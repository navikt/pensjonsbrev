package no.nav.brev.brevbaker.markup

import kotlinx.serialization.Serializable

/**
 * En ferdig bestilling som pdf-bygger kan rendre til PDF: hovedbrevet, eventuelle brevvedlegg og
 * frittstående PDF-vedlegg-titler, sammen med språk og brevtype.
 */
@ConsistentCopyVisibility
@Serializable
data class LetterPDFRequest internal constructor(
    val letterMarkup: LetterMarkup,
    val attachments: List<Attachment>,
    val pdfVedlegg: List<PDFTittel>,
    val spraak: Markup.Spraak,
    val brevtype: Markup.Brevtype,
)

/** Serialiser et [LetterPDFRequest] til JSON. Bruk dette når du bestiller en PDF fra pdf-bygger. */
fun LetterPDFRequest.toJson(): String =
    markupJson.encodeToString(LetterPDFRequest.serializer(), this)

/** Deserialiser et [LetterPDFRequest] fra JSON produsert av [toJson]. */
fun decodeLetterPDFRequest(json: String): LetterPDFRequest =
    markupJson.decodeFromString(LetterPDFRequest.serializer(), json)
