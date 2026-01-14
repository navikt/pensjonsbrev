package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import java.util.Objects

fun <Lang : LanguageSupport, LetterData : VedleggData> createAttachment(
    title: PlainTextOnlyScope<Lang, LetterData>.() -> Unit,
    includeSakspart: Boolean = false,
    outline: OutlineOnlyScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    PlainTextOnlyScope<Lang, LetterData>().apply(title).elements,
    OutlineOnlyScope<Lang, LetterData>().apply(outline).elements,
    includeSakspart
)

class IncludeAttachment<out Lang : LanguageSupport, AttachmentData : VedleggData> internal constructor(
    val data: Expression<AttachmentData>,
    val template: AttachmentTemplate<Lang, AttachmentData>,
    val predicate: Expression<Boolean> = Expression.Literal(true),
): StableHash by StableHash.of(data, template, predicate) {
    override fun equals(other: Any?): Boolean {
        if (other !is IncludeAttachment<*, *>) return false
        return data == other.data && template == other.template && predicate == other.predicate
    }
    override fun hashCode() = Objects.hash(data, template, predicate)
    override fun toString() = "IncludeAttachment(data=$data, template=$template, predicate=$predicate)"
}

class AttachmentTemplate<out Lang : LanguageSupport, AttachmentData : VedleggData> internal constructor(
    val title: List<TextElement<Lang>>,
    val outline: List<OutlineElement<Lang>>,
    val includeSakspart: Boolean = false,
): HasModel<AttachmentData>, StableHash by StableHash.of(StableHash.of(title), StableHash.of(outline), StableHash.of(includeSakspart)) {
    override fun equals(other: Any?): Boolean {
        if (other !is AttachmentTemplate<*, *>) return false
        return title == other.title && outline == other.outline && includeSakspart == other.includeSakspart
    }
    override fun hashCode() = Objects.hash(title, outline, includeSakspart)
    override fun toString() = "AttachmentTemplate(title=$title, outline=$outline, includeSakspart=$includeSakspart)"

}

class AlltidValgbartVedlegg<out Lang : LanguageSupport>(
    val vedlegg: AttachmentTemplate<Lang, EmptyVedleggData>,
    val kode: AlltidValgbartVedleggKode
) : HasModel<EmptyVedleggData>, StableHash by StableHash.of(vedlegg, StableHash.of(kode.kode())) {
    fun asIncludeAttachment() = IncludeAttachment(EmptyVedleggData.expr(), vedlegg)
    override fun equals(other: Any?): Boolean {
        if (other !is AlltidValgbartVedlegg<*>) return false
        return vedlegg == other.vedlegg && kode == other.kode
    }
    override fun hashCode() = Objects.hash(vedlegg, kode)
    override fun toString() = "AlltidValgbartVedlegg(vedlegg=$vedlegg, kode=$kode)"
}