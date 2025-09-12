package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.ExpressionScope
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import java.util.Objects

class PDFTemplate<out Lang : LanguageSupport, AttachmentData : PDFVedleggData>(
    val data: Expression<AttachmentData>,
    private val transform: (AttachmentData) -> PDFVedlegg
) : StableHash by StableHash.of( data) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFTemplate<*, *>) return false
        return data == other.data && other.transform == transform
    }

    fun doTransform(scope: ExpressionScope<*>) = transform(data.eval(scope))

    override fun hashCode() = Objects.hash(data, transform)
    override fun toString() = "PDFTemplate(data=$data, transform=$transform)"
}