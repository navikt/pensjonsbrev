package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import no.nav.pensjon.brevbaker.api.model.VedleggType
import java.util.Objects

class PDFTemplate<out Lang: LanguageSupport, AttachmentData : PDFVedleggData>(
    val type: VedleggType,
    val data: Expression<AttachmentData>
) : StableHash by StableHash.of(StableHash.of(type.name), StableHash.of(type.tittel.entries.joinToString()),data) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFTemplate<*,*>) return false
        return type == other.type && data == other.data
    }
    override fun hashCode() = Objects.hash(type, data)
    override fun toString() = "PDFTemplate(type=$type, data=$data)"
}