package no.nav.brev.brevbaker

import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.Letter2MarkupV2
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Attachment
import no.nav.pensjon.brevbaker.api.model.LetterMarkupV2

object LetterTestRenderer {
    fun render(letter: Letter<*>): LetterAndAttachmentsMarkup = Letter2Markup.render(letter)
        .let { LetterAndAttachmentsMarkup(it.letterMarkup, it.attachments) }

    fun renderLetterOnly(letter: Letter<*>) = Letter2Markup.renderLetterOnly(letter.toScope(), letter.template)

    fun renderAttachmentsOnly(scope: ExpressionScope<*>, template: LetterTemplate<*, *>) = Letter2Markup.renderAttachmentsOnly(scope, template)

    fun renderLetterOnlyV2(letter: Letter<*>): LetterMarkupV2 = Letter2MarkupV2.renderLetterOnly(letter.toScope(), letter.template)

    fun renderAttachmentsOnlyV2(scope: ExpressionScope<*>, template: LetterTemplate<*, *>): List<LetterMarkupV2.Attachment> =
        Letter2MarkupV2.renderAttachmentsOnly(scope, template)
}

data class LetterAndAttachmentsMarkup(val letterMarkup: LetterMarkup, val attachments: List<Attachment>)