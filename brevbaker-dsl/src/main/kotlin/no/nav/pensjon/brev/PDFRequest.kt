package no.nav.pensjon.brev

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@Suppress("unused")
data class PDFRequest(
    val letterMarkup: LetterMarkup,
    val attachments: List<LetterMarkup.Attachment>,
    val language: Language,
    val felles: Felles,
    val brevtype: LetterMetadata.Brevtype,
)