package no.nav.brev.brevbaker.markup

import kotlinx.serialization.json.Json

/**
 * Konfigurert [Json]-instans for (de)serialisering av markup-modellen. Intern med vilje – konsumenter
 * skal bruke de typede hjelpefunksjonene co-lokalisert med hver hele request/response-type (f.eks.
 * [LetterPDFRequest.toJson]/[decodeLetterPDFRequest]) slik at serialiseringsoppsettet holdes likt.
 */
internal val markupJson: Json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    classDiscriminator = "type"
}
