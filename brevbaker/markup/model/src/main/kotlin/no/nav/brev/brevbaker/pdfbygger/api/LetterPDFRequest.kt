package no.nav.brev.brevbaker.pdfbygger.api

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.PDFTittel

/**
 * En ferdig bestilling som pdf-bygger kan rendre til PDF: hovedbrevet, eventuelle brevvedlegg og
 * frittstående PDF-vedlegg-titler, sammen med språk og brevtype.
 */
@ConsistentCopyVisibility
data class LetterPDFRequest internal constructor(
    val letterMarkup: LetterMarkup,
    val attachments: List<Attachment>,
    val pdfVedlegg: List<PDFTittel>,
    val spraak: Markup.Spraak,
    val brevtype: Markup.Brevtype,
)

/**
 * Konstruerer en [LetterPDFRequest].
 *
 * Konstruktøren er `internal` slik at `copy()` forblir skjult, og denne fabrikken er den eneste veien inn
 * utenfra. Vedlegg og PDF-vedlegg-titler bygges med `attachment`- og `pdfTittel`-DSL-ene i
 * `brevbaker:markup:dsl` og sendes inn som lister.
 */
fun letterPDFRequest(
    letterMarkup: LetterMarkup,
    spraak: Markup.Spraak,
    brevtype: Markup.Brevtype,
    attachments: List<Attachment> = emptyList(),
    pdfVedlegg: List<PDFTittel> = emptyList(),
): LetterPDFRequest = LetterPDFRequest(
    letterMarkup = letterMarkup,
    attachments = attachments,
    pdfVedlegg = pdfVedlegg,
    spraak = spraak,
    brevtype = brevtype,
)
