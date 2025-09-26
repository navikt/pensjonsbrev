package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.TextElement
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import java.util.Objects

interface PDFTemplate<out Lang : LanguageSupport, AttachmentData : PDFVedleggData> {
    val title: List<TextElement<Lang>>
    fun template(data: AttachmentData): PDFVedlegg

    fun createVedlegg(scope: ExpressionScope<*>, data: Expression<AttachmentData>) = template(data.eval(scope))
}

class IncludeAttachmentPDF<out Lang : LanguageSupport, AttachmentData : PDFVedleggData>(
    val data: Expression<AttachmentData>,
    val template: PDFTemplate<Lang, AttachmentData>,
) {
    fun eval(scope: ExpressionScope<*>) = template.createVedlegg(scope, data)

    override fun equals(other: Any?): Boolean {
        if (other !is IncludeAttachmentPDF<*, *>) return false
        return data == other.data && template == other.template
    }

    override fun hashCode() = Objects.hash(data, template)
    override fun toString() = "IncludeAttachmentPDF(data=$data, template=$template)"
}

fun <Lang : LanguageSupport, AttachmentData : PDFVedleggData> createAttachmentPDF(
    title: List<TextElement<Lang>>,
    init: PDFVedlegg.(data: AttachmentData) -> Unit,
): PDFTemplate<Lang, AttachmentData> =
    object : PDFTemplate<Lang, AttachmentData> {
        override val title = title
        override fun template(data: AttachmentData): PDFVedlegg =
            PDFVedlegg().apply { init(data) }
    }