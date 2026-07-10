package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.LiteralOrExpressionBuilder.LiteralOrExpression
import no.nav.pensjon.brev.template.dsl.TextContentCreator.createTextContent
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.validation.BrevTemplateValidator
import no.nav.pensjon.brev.template.validation.EmptyValidator
import no.nav.pensjon.brev.template.vedlegg.IncludeAttachmentPDF
import no.nav.pensjon.brev.template.vedlegg.PDFTemplate
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData

@LetterTemplateMarker
class TemplateRootScope<Lang : LanguageSupport, LetterData : Any> internal constructor(
    private val validator: BrevTemplateValidator = EmptyValidator,
) : TemplateGlobalScope<LetterData> {
    internal val title: List<TextElement<Lang>> field: MutableList<TextElement<Lang>> = mutableListOf()
    internal val outline: List<OutlineElement<Lang>> field: MutableList<OutlineElement<Lang>> = mutableListOf()
    internal val attachments: List<IncludeAttachment<Lang, *>> field: MutableList<IncludeAttachment<Lang, *>> = mutableListOf()
    internal val pdfAttachments: List<IncludeAttachmentPDF<Lang, *>> field: MutableList<IncludeAttachmentPDF<Lang, *>> = mutableListOf()
    internal fun lagreSaksbehandlervalg(key: String, verdi: SaksbehandlervalgVerdi<*>) {
        require(saksbehandlervalg.containsKey(key).not()) { "Saksbehandlervalg med id $key allerede definert" }
        saksbehandlervalg[key] = verdi
    }

    fun title(init: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        title.addAll(PlainTextOnlyScope<Lang, LetterData>().apply(init).elements)
    }

    fun outline(init: OutlineOnlyScope<Lang, LetterData>.() -> Unit) {
        outline.addAll(OutlineOnlyScope<Lang, LetterData>(validator.subScope()).apply(init).elements)
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

    @RequiresOptIn(
        message = "Redigerbare vedlegg skal kun brukes etter avtale. Bruk @OptIn(RedigerbartVedlegg::class) på malen når bruken er avklart.",
        level = RequiresOptIn.Level.ERROR,
    )
    @Retention(AnnotationRetention.BINARY)
    @Target(AnnotationTarget.FUNCTION)
    annotation class RedigerbartVedlegg

    /**
     * Opt-in: gjør et vedlegg redigerbart. Saksbehandler kan overstyre innholdet i Skribenten,
     * og overstyringen identifiseres med [vedleggId]. Skal kun brukes for vedlegg som bevisst
     * gjøres redigerbare (de aller fleste vedlegg skal ikke være det). [vedleggId] må være
     * stabil og unik innenfor brevet.
     */
    @RedigerbartVedlegg
    fun <AttachmentData : VedleggData> includeAttachmentRedigerbar(
        vedleggId: VedleggId,
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        attachments.add(IncludeAttachment(attachmentData, template, predicate, vedleggId))
    }

    @RedigerbartVedlegg
    fun includeAttachmentRedigerbar(
        vedleggId: VedleggId,
        template: AttachmentTemplate<Lang, EmptyVedleggData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        attachments.add(IncludeAttachment(EmptyVedleggData.expr(), template, predicate, vedleggId))
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
