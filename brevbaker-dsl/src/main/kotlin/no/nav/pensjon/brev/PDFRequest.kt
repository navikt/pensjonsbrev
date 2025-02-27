package no.nav.pensjon.brev

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

interface PDFRequest {
    val letterMarkup: LetterMarkup
    val attachments: List<LetterMarkup.Attachment>
    val language: LanguageCode
    val felles: Felles
    val brevtype: LetterMetadata.Brevtype
}

@Suppress("unused")
@InterneDataklasser
data class PDFRequestImpl(
    override val letterMarkup: LetterMarkup,
    override val attachments: List<LetterMarkup.Attachment>,
    override val language: LanguageCode,
    override val felles: Felles,
    override val brevtype: LetterMetadata.Brevtype,
) : PDFRequest