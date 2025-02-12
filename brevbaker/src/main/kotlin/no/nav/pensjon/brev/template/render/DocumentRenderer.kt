package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

interface DocumentRenderer<R : Any> {
    fun render(
        letter: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        language: Language,
        felles: Felles,
        brevtype: LetterMetadata.Brevtype,
    ): R

    fun render(
        letterMarkup: LetterMarkup,
        attachments: List<LetterMarkup.Attachment>,
        letter: Letter<*>,
    ): R =
        render(letterMarkup, attachments, letter.language, letter.felles, letter.template.letterMetadata.brevtype)
}
