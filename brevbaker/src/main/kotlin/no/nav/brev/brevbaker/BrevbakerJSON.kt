package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

internal object BrevbakerJSON {
    fun renderJSON(letter: Letter<BrevbakerBrevdata>): LetterMarkup = Letter2Markup.renderLetterOnly(letter.toScope(), letter.template)
}