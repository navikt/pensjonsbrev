package no.nav.brev.brevbaker

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.brev.brevbaker.template.render.Letter2Markup
import no.nav.brev.brevbaker.template.render.Letter2MarkupV2
import no.nav.brev.brevbaker.template.render.toMarkup
import no.nav.pensjon.brev.template.expression.SelectorUsage
import no.nav.brev.brevbaker.template.toScope
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl
import no.nav.brev.brevbaker.markup.LetterMarkup as MarkupLetterMarkup
import no.nav.brev.brevbaker.markup.Attachment as MarkupAttachment
import no.nav.brev.brevbaker.markup.LetterMarkupWithDataUsage as MarkupLetterMarkupWithDataUsage
import no.nav.brev.brevbaker.markup.outline.Text as MarkupText
import no.nav.brev.brevbaker.markup.dsl.dataUsageProperty
import no.nav.brev.brevbaker.markup.dsl.letterMarkupExtended
import no.nav.brev.brevbaker.markup.dsl.letterMarkupWithDataUsage

internal object BrevbakerLetterMarkup {
    fun <T : BrevbakerBrevdata> renderLetterMarkup(letter: Letter<T>): LetterMarkup = Letter2Markup.renderLetterOnly(letter.toScope(), letter.template)

    fun <T : BrevbakerBrevdata> renderRedigerbartVedleggTitler(letter: Letter<T>): Map<VedleggId, List<LetterMarkup.ParagraphContent.Text>> =
        Letter2Markup.renderEditableAttachmentTitles(letter.toScope(), letter.template)

    fun <T : BrevbakerBrevdata> renderRedigerbartVedlegg(letter: Letter<T>, vedleggId: VedleggId): LetterMarkup.Attachment? =
        Letter2Markup.renderEditableAttachment(letter.toScope(), letter.template, vedleggId)

    @OptIn(InterneDataklasser::class)
    fun <T : BrevbakerBrevdata> renderLetterMarkupWithDataUsage(letter: Letter<T>): LetterMarkupWithDataUsage =
        SelectorUsage().let { usage ->
            LetterMarkupWithDataUsageImpl(
                Letter2Markup.renderLetterOnly(letter.toScope(usage), letter.template),
                usage.propertyUsage,
                letter.template.letterMetadata.brevtype
            )
        }

    fun <T : BrevbakerBrevdata> renderLetterMarkupV2(letter: Letter<T>): MarkupLetterMarkup = Letter2MarkupV2.renderLetterOnly(letter.toScope(), letter.template)

    fun <T : BrevbakerBrevdata> renderRedigerbartVedleggV2Titler(letter: Letter<T>): Map<VedleggId, List<MarkupText>> =
        Letter2MarkupV2.renderEditableAttachmentTitles(letter.toScope(), letter.template)

    fun <T : BrevbakerBrevdata> renderRedigerbartVedleggV2(letter: Letter<T>, vedleggId: VedleggId): MarkupAttachment? =
        Letter2MarkupV2.renderEditableAttachment(letter.toScope(), letter.template, vedleggId)

    fun <T : BrevbakerBrevdata> renderLetterMarkupWithDataUsageV2(letter: Letter<T>): MarkupLetterMarkupWithDataUsage =
        SelectorUsage().let { usage ->
            val scope = letter.toScope(usage)
            letterMarkupWithDataUsage(
                markup = letterMarkupExtended(
                    saksinformasjon = Letter2MarkupV2.buildSaksinformasjon(scope),
                    signatur = Letter2MarkupV2.buildSignatur(scope),
                    build = Letter2MarkupV2.buildLetter(
                        scope = scope,
                        template = letter.template
                    )
                ),
                brevtype = letter.template.letterMetadata.brevtype.toMarkup(),
                letterDataUsage = usage.propertyUsage.map {
                    dataUsageProperty(it.typeName, it.propertyName)
                }.toSet()
            )
        }
}