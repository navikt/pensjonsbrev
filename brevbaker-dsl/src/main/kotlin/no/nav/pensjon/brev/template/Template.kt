package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyVedlegg
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
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

    fun includeAttachment(template: AttachmentTemplate<Lang, EmptyVedlegg>) {
        attachments.add(IncludeAttachment(EmptyVedlegg.expr(), template, true.expr()))
    }

    fun <AttachmentData : Any> includeAttachment(
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
        template: AttachmentTemplate<Lang, EmptyVedlegg>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        attachments.add(IncludeAttachment(EmptyVedlegg.expr(), template, predicate))
    }

    fun <AttachmentData : Any> includeAttachmentIfNotNull(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData?>,
    ) {
        @Suppress("UNCHECKED_CAST")
        attachments.add(IncludeAttachment(attachmentData as Expression<AttachmentData>, template, attachmentData.notNull()))
    }
}


@LetterTemplateMarker
class TemplateFormChoiceScope<Lang : LanguageSupport, LetterData : Any> internal constructor(
    val choices: MutableList<Element.OutlineContent.ParagraphContent.Text<Lang>> = mutableListOf()
) : TemplateGlobalScope<LetterData>

fun <Lang1 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Single<Lang1>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    fontType: FontType = FontType.PLAIN
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, fontType).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Double<Lang1, Lang2>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, fontType).also { choices.add(it) }
}

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language, LetterData : Any> TemplateFormChoiceScope<LanguageSupport.Triple<Lang1, Lang2, Lang3>, LetterData>.choice(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = FontType.PLAIN,
) {
    Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, lang3, fontType).also { choices.add(it) }
}

@DslMarker
internal annotation class LetterTemplateMarker
