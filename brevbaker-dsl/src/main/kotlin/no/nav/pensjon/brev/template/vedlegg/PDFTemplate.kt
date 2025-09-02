package no.nav.pensjon.brev.template.vedlegg

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import java.util.Objects

class PDFTemplate<out Lang: LanguageSupport, AttachmentData : PDFVedleggData>(
    val name: String,
    val tittel: Map<LanguageCode, String>,
    val data: Expression<AttachmentData>
) : StableHash by StableHash.of(StableHash.of(name), StableHash.of(tittel.entries.joinToString()),data) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFTemplate<*,*>) return false
        return tittel == other.tittel && data == other.data
    }
    override fun hashCode() = Objects.hash(tittel, data)
    override fun toString() = "PDFTemplate(tittel=$tittel, data=$data)"
}