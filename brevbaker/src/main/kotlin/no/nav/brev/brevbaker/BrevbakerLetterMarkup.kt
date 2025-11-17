package no.nav.brev.brevbaker

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.pensjon.brev.template.expression.SelectorUsage
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl

internal object BrevbakerLetterMarkup {
    fun <T: BrevbakerBrevdata> renderLetterMarkup(letter: Letter<T>): LetterMarkup = Letter2Markup.renderLetterOnly(letter.toScope(), letter.template)

    @OptIn(InterneDataklasser::class)
    fun <T: BrevbakerBrevdata> renderLetterMarkupWithDataUsage(letter: Letter<T>): LetterMarkupWithDataUsage =
        SelectorUsage().let { usage ->
            LetterMarkupWithDataUsageImpl(
            Letter2Markup.renderLetterOnly(letter.toScope(usage), letter.template),
                usage.propertyUsage
            )
        }
}