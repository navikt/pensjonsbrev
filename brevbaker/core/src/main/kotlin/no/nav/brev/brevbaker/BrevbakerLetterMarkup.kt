package no.nav.brev.brevbaker

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.VedleggId
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.pensjon.brev.template.expression.SelectorUsage
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl

internal object BrevbakerLetterMarkup {
    fun <T: BrevbakerBrevdata> renderLetterMarkup(letter: Letter<T>): LetterMarkup = Letter2Markup.renderLetterOnly(letter.toScope(), letter.template)

    fun <T: BrevbakerBrevdata> renderRedigerbartVedleggTitler(letter: Letter<T>): Map<VedleggId, List<LetterMarkup.ParagraphContent.Text>> =
        Letter2Markup.renderEditableAttachmentTitles(letter.toScope(), letter.template)

    fun <T: BrevbakerBrevdata> renderRedigerbartVedlegg(letter: Letter<T>, vedleggId: VedleggId): LetterMarkup.Attachment? =
        Letter2Markup.renderEditableAttachment(letter.toScope(), letter.template, vedleggId)

    @OptIn(InterneDataklasser::class)
    fun <T: BrevbakerBrevdata> renderLetterMarkupWithDataUsage(letter: Letter<T>): LetterMarkupWithDataUsage =
        SelectorUsage().let { usage ->
            LetterMarkupWithDataUsageImpl(
            Letter2Markup.renderLetterOnly(letter.toScope(usage), letter.template),
                usage.propertyUsage,
                letter.template.letterMetadata.brevtype
            )
        }
}