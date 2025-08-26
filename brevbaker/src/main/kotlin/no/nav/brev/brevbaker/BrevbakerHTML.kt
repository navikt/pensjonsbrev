package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.HTMLDocumentRenderer
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brev.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

internal object BrevbakerHTML {
    fun renderHTML(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev)
            .let { HTMLDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }
            .let { html ->
                LetterResponse(
                    file = html.indexHTML.content.toByteArray(Charsets.UTF_8),
                    contentType = ContentTypes.TEXT_HTML_UTF8,
                    letterMetadata = letter.template.letterMetadata,
                )
            }

    private fun renderCompleteMarkup(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterWithAttachmentsMarkup =
        letter.toScope().let { scope ->
            LetterWithAttachmentsMarkup(
                redigertBrev ?: Letter2Markup.renderLetterOnly(scope, letter.template),
                Letter2Markup.renderAttachmentsOnly(scope, letter.template),
            )
        }
}