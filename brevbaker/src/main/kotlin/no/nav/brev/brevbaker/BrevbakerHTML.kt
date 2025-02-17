package no.nav.brev.brevbaker

import io.ktor.http.ContentType
import io.ktor.http.withCharset
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.render.HTMLDocumentRenderer
import no.nav.pensjon.brev.template.render.Letter2Markup
import no.nav.pensjon.brev.template.render.LetterWithAttachmentsMarkup
import no.nav.pensjon.brev.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup

internal object BrevbakerHTML {
    fun renderHTML(letter: Letter<BrevbakerBrevdata>, redigertBrev: LetterMarkup? = null): LetterResponse =
        renderCompleteMarkup(letter, redigertBrev)
            .let { HTMLDocumentRenderer.render(it.letterMarkup, it.attachments, letter) }
            .let { html ->
                LetterResponse(
                    file = html.indexHTML.content.toByteArray(Charsets.UTF_8),
                    contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8).toString(),
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