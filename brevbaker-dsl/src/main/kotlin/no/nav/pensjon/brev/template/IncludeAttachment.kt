package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brevbaker.api.model.PDFVedleggData
import no.nav.pensjon.brevbaker.api.model.VedleggType
import java.util.Objects

fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
    title: TextElement<Lang>,
    includeSakspart: Boolean = false,
    outline: OutlineOnlyScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    listOf(title),
    OutlineOnlyScope<Lang, LetterData>().apply(outline).elements,
    includeSakspart
)

fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
    title: PlainTextOnlyScope<Lang, LetterData>.() -> Unit,
    includeSakspart: Boolean = false,
    outline: OutlineOnlyScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    PlainTextOnlyScope<Lang, LetterData>().apply(title).elements,
    OutlineOnlyScope<Lang, LetterData>().apply(outline).elements,
    includeSakspart
)

class IncludeAttachment<out Lang : LanguageSupport, AttachmentData : Any> internal constructor(
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

class AttachmentTemplate<out Lang : LanguageSupport, AttachmentData : Any> internal constructor(
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

class PDFTemplate<AttachmentData : PDFVedleggData>(
    val type: VedleggType,
    val data: Expression<AttachmentData>
) : StableHash by StableHash.of(StableHash.of(type.name+type.tittel), data) {
    override fun equals(other: Any?): Boolean {
        if (other !is PDFTemplate<*>) return false
        return type == other.type && data == other.data
    }
    override fun hashCode() = Objects.hash(type, data)
    override fun toString() = "PDFTemplate(type=$type, data=$data)"
}