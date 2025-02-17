package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.Letter2Markup

object BrevbakerJSON {
    fun renderJSON(letter: Letter<BrevbakerBrevdata>) = Letter2Markup.render(letter).letterMarkup
}