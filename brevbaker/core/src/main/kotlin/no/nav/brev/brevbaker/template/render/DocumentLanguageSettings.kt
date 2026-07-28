package no.nav.brev.brevbaker.template.render

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import no.nav.pensjon.brev.template.Language

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
    companion object {
        private val LANGUAGE_KEYS = listOf(Language.Bokmal, Language.Nynorsk, Language.English).map(::spraakNoekkel)

        private fun spraakNoekkel(language: Language) = when (language) {
            Language.Bokmal -> "bokmal"
            Language.Nynorsk -> "nynorsk"
            Language.English -> "english"
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

        operator fun invoke(language: Language): DocumentLanguageSettings = alleSpraak.getValue(spraakNoekkel(language))
    }
}
