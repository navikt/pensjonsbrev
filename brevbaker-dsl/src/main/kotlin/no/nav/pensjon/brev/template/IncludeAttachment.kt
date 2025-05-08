package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.TextScope
import no.nav.pensjon.brev.template.dsl.text
import java.util.Objects

fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
    title: TextElement<Lang>,
    includeSakspart: Boolean = false,
    outline: OutlineOnlyScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    title,
    OutlineOnlyScope<Lang, LetterData>().apply(outline).elements,
    includeSakspart
)

fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
    title: PlainTextOnlyScope<Lang, LetterData>.() -> Unit,
    includeSakspart: Boolean = false,
    outline: OutlineOnlyScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    PlainTextOnlyScope<Lang, LetterData>().apply(title).elements.first(),
    OutlineOnlyScope<Lang, LetterData>().apply(outline).elements,
    includeSakspart
)

fun TextScope<BaseLanguages, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Language.Bokmal to "«", Language.Nynorsk to "«", Language.English to "'")
    addTextContent(attachment.title)
    text(Language.Bokmal to "»", Language.Nynorsk to "»", Language.English to "'")
}

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
    val title: TextElement<Lang>,
    val outline: List<OutlineElement<Lang>>,
    val includeSakspart: Boolean = false,
): HasModel<AttachmentData>, StableHash by StableHash.of(title, StableHash.of(outline), StableHash.of(includeSakspart)) {
    override fun equals(other: Any?): Boolean {
        if (other !is AttachmentTemplate<*, *>) return false
        return title == other.title && outline == other.outline && includeSakspart == other.includeSakspart
    }
    override fun hashCode() = Objects.hash(title, outline, includeSakspart)
    override fun toString() = "AttachmentTemplate(title=$title, outline=$outline, includeSakspart=$includeSakspart)"

}