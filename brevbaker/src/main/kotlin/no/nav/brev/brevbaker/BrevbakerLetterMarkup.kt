package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brev.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

internal object BrevbakerLetterMarkup {
    fun renderLetterMarkup(letter: Letter<BrevbakerBrevdata>): LetterMarkup = Letter2Markup.renderLetterOnly(letter.toScope(), letter.template)
    fun renderLetterWithAttachmentsMarkup(letter: Letter<BrevbakerBrevdata>): LetterWithAttachmentsMarkup = Letter2Markup.renderLetter(letter.toScope(), letter.template)
}