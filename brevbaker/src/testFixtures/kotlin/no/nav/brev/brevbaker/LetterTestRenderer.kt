package no.nav.brev.brevbaker

import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brev.template.toScope

object LetterTestRenderer {
    fun render(letter: Letter<*>): LetterWithAttachmentsMarkup = Letter2Markup.render(letter)

    fun renderLetterOnly(letter: Letter<*>) = Letter2Markup.renderLetterOnly(letter.toScope(), letter.template)

    fun renderAttachmentsOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>) = Letter2Markup.renderAttachmentsOnly(scope, template)
}