package no.nav.brev.brevbaker.document

import no.nav.brev.brevbaker.markup.Markup
import java.util.Objects

/**
 * En bestilling som pdf-bygger kan rendre til PDF via `/produserDokument`.
 *
 * Til forskjell fra `LetterPDFRequest` har et dokument verken vedlegg eller brevtype: [spraak]
 * trengs kun for å velge ledetekster i saksinformasjonen og formatere dokumentdatoen.
 */
class DocumentPDFRequest internal constructor(
    val document: Document,
    val spraak: Markup.Spraak,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is DocumentPDFRequest) return false
        return document == other.document && spraak == other.spraak
    }

    override fun hashCode() = Objects.hash(document, spraak)

    override fun toString(): String = "DocumentPDFRequest(document=$document, spraak=$spraak)"
}
