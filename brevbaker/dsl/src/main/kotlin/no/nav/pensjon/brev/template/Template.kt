package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.*
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
    private val _title: MutableList<TextElement<Lang>> = mutableListOf()
    internal val title: List<TextElement<Lang>> get() = _title
    private val _outline: MutableList<OutlineElement<Lang>> = mutableListOf()
    internal val outline: List<OutlineElement<Lang>> get() = _outline
    private val _attachments: MutableList<IncludeAttachment<Lang, *>> = mutableListOf()
    internal val attachments: List<IncludeAttachment<Lang, *>> get() = _attachments
    private val _pdfAttachments: MutableList<IncludeAttachmentPDF<Lang, *>> = mutableListOf()
    internal val pdfAttachments: List<IncludeAttachmentPDF<Lang, *>> get() = _pdfAttachments

    fun title(init: PlainTextOnlyScope<Lang, LetterData>.() -> Unit) {
        _title.addAll(PlainTextOnlyScope<Lang, LetterData>().apply(init).elements)
    }

    fun outline(init: OutlineOnlyScope<Lang, LetterData>.() -> Unit) {
        _outline.addAll(OutlineOnlyScope<Lang, LetterData>(validator.subScope()).apply(init).elements)
    }

    fun <AttachmentData : VedleggData> includeAttachment(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        _attachments.add(IncludeAttachment(attachmentData, template, predicate))
    }

    fun <AttachmentData : PDFVedleggData> includeAttachment(
        template: PDFTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData>,
    ) {
        _pdfAttachments.add(IncludeAttachmentPDF(attachmentData, template))
    }

    fun includeAttachment(
        template: AttachmentTemplate<Lang, EmptyVedleggData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        _attachments.add(IncludeAttachment(EmptyVedleggData.expr(), template, predicate))
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
        _attachments.add(IncludeAttachment(attachmentData, template, predicate, vedleggId))
    }

    @RedigerbartVedlegg
    fun includeAttachmentRedigerbar(
        vedleggId: VedleggId,
        template: AttachmentTemplate<Lang, EmptyVedleggData>,
        predicate: Expression<Boolean> = true.expr(),
    ) {
        _attachments.add(IncludeAttachment(EmptyVedleggData.expr(), template, predicate, vedleggId))
    }

    fun <AttachmentData : VedleggData> includeAttachmentIfNotNull(
        template: AttachmentTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData?>,
    ) {
        @Suppress("UNCHECKED_CAST")
        _attachments.add(IncludeAttachment(attachmentData as Expression<AttachmentData>, template, attachmentData.notNull()))
    }

    fun <AttachmentData : PDFVedleggData> includeAttachmentIfNotNull(
        template: PDFTemplate<Lang, AttachmentData>,
        attachmentData: Expression<AttachmentData?>,
    ) {
        @Suppress("UNCHECKED_CAST")
        _pdfAttachments.add(IncludeAttachmentPDF(attachmentData as Expression<AttachmentData>, template, attachmentData.notNull()))
    }

}


@DslMarker
internal annotation class LetterTemplateMarker
