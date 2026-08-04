package no.nav.brev.brevbaker.pdfbygger.api

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.MarkupModelApi
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
 * Konstruerer en [LetterPDFRequest] direkte, uten `letterPDFRequest`-DSL-en i `brevbaker:markup:dsl`.
 *
 * Samme avveining som for markup-modellen: konstruktøren er `internal` slik at `copy()` forblir skjult,
 * og denne fabrikken er den eneste veien inn utenfra. Gjenbruker [MarkupModelApi] som opt-in-markør —
 * det er samme beslutning konsumenten tar ("jeg bygger modellen selv i stedet for å bruke DSL-en"), og
 * to markører hadde bare vært mer å slå på.
 */
@MarkupModelApi
fun letterPDFRequestModel(
    letterMarkup: LetterMarkup,
    attachments: List<Attachment>,
    pdfVedlegg: List<PDFTittel>,
    spraak: Markup.Spraak,
    brevtype: Markup.Brevtype,
): LetterPDFRequest = LetterPDFRequest(
    letterMarkup = letterMarkup,
    attachments = attachments,
    pdfVedlegg = pdfVedlegg,
    spraak = spraak,
    brevtype = brevtype,
)
