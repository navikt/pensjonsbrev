package no.nav.pensjon.brev.pdfbygger.typst.documentrender

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import no.nav.pensjon.brevbaker.api.model.LanguageCode

@Serializable
internal data class DocumentLanguageSettings(
    val navnprefix: String,
    val annenmottakerprefix: String,
    val vedlegggjeldernavnprefix: String,
    val gjeldernavnprefix: String,
    val saksnummerprefix: String,
    val foedselsnummerprefix: String,
    val sidesaksnummerprefix: String,
    val sideprefix: String,
    val sideinfix: String,
    val closinggreeting: String,
    val closingautomatisktextinfobrev: String,
    val closingautomatisktextvedtaksbrev: String,
    val closingvedleggprefix: String,
    val tablenextpagecontinuation: String,
    val tablecontinuedfrompreviouspage: String,
    val alttextlogo: String,
) {
    fun asMap(): Map<String, String> =
        Json.encodeToJsonElement(this).jsonObject.mapValues { (_, tekst) -> tekst.jsonPrimitive.content }

    companion object {
        private val LANGUAGE_KEYS = LanguageCode.entries.map { it.name.lowercase() }

        private fun spraakNoekkel(language: LanguageCode) = when (language) {
            LanguageCode.BOKMAL -> "bokmal"
            LanguageCode.NYNORSK -> "nynorsk"
            LanguageCode.ENGLISH -> "english"
        }

        private val alleSpraak: Map<String, DocumentLanguageSettings> =
            LANGUAGE_KEYS.associateWith { spraak ->
                val resource = "/brevbaker/documentLanguageSettings/$spraak.json"
                val innhold = DocumentLanguageSettings::class.java.getResourceAsStream(resource)
                    ?.bufferedReader()?.use { it.readText() }
                    ?: throw IllegalStateException("Fant ikke $resource på classpath")

                try {
                    Json.decodeFromString<DocumentLanguageSettings>(innhold)
                } catch (e: SerializationException) {
                    throw IllegalStateException("Kunne ikke lese språktekstene i $resource: ${e.message}", e)
                }
            }

        operator fun invoke(language: LanguageCode): DocumentLanguageSettings = alleSpraak.getValue(spraakNoekkel(language))
    }
}
