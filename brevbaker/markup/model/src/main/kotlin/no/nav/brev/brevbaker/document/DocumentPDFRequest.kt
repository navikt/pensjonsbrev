package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.markup.Markup

/**
 * En ferdig bestilling som pdf-bygger kan rendre til PDF via `/produserDokument`.
 *
 * Til forskjell fra `LetterPDFRequest` har et dokument verken vedlegg eller brevtype: [spraak]
 * trengs kun for å velge ledetekster i saksinformasjonen og formatere dokumentdatoen.
 */
@ConsistentCopyVisibility
data class DocumentPDFRequest internal constructor(
    val document: Document,
    val spraak: Markup.Spraak,
)
