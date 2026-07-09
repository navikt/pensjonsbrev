package no.nav.brev.brevbaker.markup

import kotlinx.serialization.json.Json

/**
 * Konfigurert [Json]-instans for (de)serialisering av markup-modellen.
 *
 * Bruk denne når du bestiller en PDF fra pdf-bygger basert på en [LetterMarkupV2] bygget med DSL-en.
 */
val MarkupJson: Json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    classDiscriminator = "type"
}
