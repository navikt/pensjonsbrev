package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.TextElement
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import java.util.Objects

interface PDFTemplate<out Lang : LanguageSupport, AttachmentData : PDFVedleggData, SaksbehandlerValg: SaksbehandlerValgBrevdata> {
    val title: List<TextElement<Lang>>
    fun template(data: AttachmentData, felles: Felles, saksbehandlerValg: SaksbehandlerValg): PDFVedlegg

    fun createVedlegg(scope: ExpressionScope<*>, data: Expression<AttachmentData>, saksbehandlerValg: Expression<SaksbehandlerValg>) =
        template(data.eval(scope), scope.felles, saksbehandlerValg.eval(scope))
}

class IncludeAttachmentPDF<out Lang : LanguageSupport, AttachmentData : PDFVedleggData, SaksbehandlerValg : SaksbehandlerValgBrevdata>(
    val data: Expression<AttachmentData>,
    val template: PDFTemplate<Lang, AttachmentData, SaksbehandlerValg>,
    val saksbehandlerValg: Expression<SaksbehandlerValg>,
) {
    fun eval(scope: ExpressionScope<*>) = template.createVedlegg(scope, data, saksbehandlerValg)

    override fun equals(other: Any?): Boolean {
        if (other !is IncludeAttachmentPDF<*, *, *>) return false
        return data == other.data && template == other.template
    }

    override fun hashCode() = Objects.hash(data, template)
    override fun toString() = "IncludeAttachmentPDF(data=$data, template=$template)"
}

fun <Lang : LanguageSupport, AttachmentData : PDFVedleggData, SaksbehandlerValg : SaksbehandlerValgBrevdata> createAttachmentPDF(
    title: List<TextElement<Lang>>,
    init: PDFVedlegg.(data: AttachmentData, felles: Felles, saksbehandlerValg: SaksbehandlerValg) -> Unit,
): PDFTemplate<Lang, AttachmentData, SaksbehandlerValg> =
    object : PDFTemplate<Lang, AttachmentData, SaksbehandlerValg> {
        override val title = title
        override fun template(data: AttachmentData, felles: Felles, saksbehandlerValg: SaksbehandlerValg): PDFVedlegg =
            PDFVedlegg().apply { init(data, felles, saksbehandlerValg) }
    }