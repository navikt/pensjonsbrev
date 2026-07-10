package no.nav.brev.brevbaker.markup

import kotlinx.serialization.json.Json
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text

/**
 * Konfigurert [Json]-instans for (de)serialisering av markup-modellen. Intern med vilje – konsumenter
 * skal bruke de typede hjelpefunksjonene ([toJson]/`decode*`) nedenfor slik at serialiseringsoppsettet
 * (klassediskriminator, defaults osv.) forblir en implementasjonsdetalj.
 */
internal val markupJson: Json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    classDiscriminator = "type"
}

/** Serialiser et [LetterMarkup] til JSON. Bruk dette når du bestiller en PDF fra pdf-bygger. */
fun LetterMarkup.toJson(): String = markupJson.encodeToString(LetterMarkup.serializer(), this)

/** Deserialiser et [LetterMarkup] fra JSON produsert av [toJson]. */
fun decodeLetterMarkup(json: String): LetterMarkup =
    markupJson.decodeFromString(LetterMarkup.serializer(), json)

/** Serialiser et [LetterMarkupWithDataUsage] til JSON. */
fun LetterMarkupWithDataUsage.toJson(): String =
    markupJson.encodeToString(LetterMarkupWithDataUsage.serializer(), this)

/** Deserialiser et [LetterMarkupWithDataUsage] fra JSON. */
fun decodeLetterMarkupWithDataUsage(json: String): LetterMarkupWithDataUsage =
    markupJson.decodeFromString(LetterMarkupWithDataUsage.serializer(), json)

/** Serialiser et [Attachment] til JSON. */
fun Attachment.toJson(): String = markupJson.encodeToString(Attachment.serializer(), this)

/** Deserialiser et [Attachment] fra JSON. */
fun decodeAttachment(json: String): Attachment =
    markupJson.decodeFromString(Attachment.serializer(), json)

/** Serialiser en [PDFTittel] til JSON. */
fun PDFTittel.toJson(): String = markupJson.encodeToString(PDFTittel.serializer(), this)

/** Deserialiser en [PDFTittel] fra JSON. */
fun decodePDFTittel(json: String): PDFTittel =
    markupJson.decodeFromString(PDFTittel.serializer(), json)

/** Serialiser en enkelt [Block] til JSON. */
fun Block.toJson(): String = markupJson.encodeToString(Block.serializer(), this)

/** Deserialiser en enkelt [Block] fra JSON. */
fun decodeBlock(json: String): Block = markupJson.decodeFromString(Block.serializer(), json)

/** Serialiser et enkelt [Text]-element til JSON. */
fun Text.toJson(): String = markupJson.encodeToString(Text.serializer(), this)

/** Deserialiser et enkelt [Text]-element fra JSON. */
fun decodeText(json: String): Text = markupJson.decodeFromString(Text.serializer(), json)
