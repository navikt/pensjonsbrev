package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.LiteralOrExpression
import no.nav.pensjon.brev.template.dsl.TextContentCreator.createTextContent
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.vedlegg.IncludeAttachmentPDF
import no.nav.pensjon.brev.template.vedlegg.PDFTemplate
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData

@LetterTemplateMarker
class TemplateRootScope<Lang : LanguageSupport, LetterData : Any> internal constructor(
    val title: MutableList<TextElement<Lang>> = mutableListOf(),
    val outline: MutableList<OutlineElement<Lang>> = mutableListOf(),
    val attachments: MutableList<IncludeAttachment<Lang, *>> = mutableListOf(),
    val pdfAttachments: MutableList<IncludeAttachmentPDF<Lang, *>> = mutableListOf(),
) : TemplateGlobalScope<LetterData> {

    fun title(init: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        title.addAll(PlainTextOnlyScope<Lang, LetterData>().apply(init).elements)
    }

    fun outline(init: OutlineOnlyScope<Lang, LetterData>.() -> Unit) {
        outline.addAll(OutlineOnlyScope<Lang, LetterData>().apply(init).elements)
    }

    fun <AttachmentData : VedleggData> includeAttachment(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        attachments.add(IncludeAttachment(attachmentData, template, predicate))
    }

    fun <AttachmentData : PDFVedleggData> includeAttachment(
        template: PDFTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData>,
    ) {
        pdfAttachments.add(IncludeAttachmentPDF(attachmentData, template))
    }

    fun includeAttachment(
        template: AttachmentTemplate<Lang, EmptyVedleggData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        attachments.add(IncludeAttachment(EmptyVedleggData.expr(), template, predicate))
    }

    fun <AttachmentData : VedleggData> includeAttachmentIfNotNull(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData?>,
    ) {
        @Suppress("UNCHECKED_CAST")
        attachments.add(IncludeAttachment(attachmentData as Expression<AttachmentData>, template, attachmentData.notNull()))
    }

    fun <AttachmentData : PDFVedleggData> includeAttachmentIfNotNull(
        template: PDFTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData?>,
    ) {
        @Suppress("UNCHECKED_CAST")
        pdfAttachments.add(IncludeAttachmentPDF(attachmentData as Expression<AttachmentData>, template, attachmentData.notNull()))
    }

}


@LetterTemplateMarker
class TemplateFormChoiceScope<Lang : LanguageSupport, LetterData : Any> internal constructor(
    val choices: MutableList<Element.OutlineContent.ParagraphContent.Text<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>

fun <Lang1 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Single<Lang1>, LetterData>.choice(
    lang1: Pair<Lang1, LiteralOrExpression>,
    fontType: FontType = FontType.PLAIN
) {
    createTextContent(lang1, fontType).also { choices.add(it)}
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Double<Lang1, Lang2>, LetterData>.choice(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    createTextContent(lang1, lang2, fontType).also { choices.add(it)}
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, LetterData>.choice(
    lang1: Pair<Lang1, LiteralOrExpression>,
    lang2: Pair<Lang2, LiteralOrExpression>,
    lang3: Pair<Lang3, LiteralOrExpression>,
    fontType: FontType = FontType.PLAIN,
) {
    createTextContent(lang1, lang2, lang3, fontType).also { choices.add(it)}
}

@DslMarker
internal annotation class LetterTemplateMarker
