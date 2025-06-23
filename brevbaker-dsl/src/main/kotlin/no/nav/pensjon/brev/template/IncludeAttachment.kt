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

private const val bokmalstart = "«"
private const val nynorskstart = "«"
private const val englishstart = "'"

private const val bokmalend = "»"
private const val nynorskend = "»"
private const val englishend = "'"

fun TextScope<BaseLanguages, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Language.Bokmal to bokmalstart, Language.Nynorsk to nynorskstart, Language.English to englishstart)
    attachment.title.forEach { addTextContent(it) }
    text(Language.Bokmal to bokmalend, Language.Nynorsk to nynorskend, Language.English to englishend)
}

@JvmName("namedReferenceBokmalNynorsk")
fun TextScope<LangBokmalNynorsk, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Language.Bokmal to bokmalstart, Language.Nynorsk to nynorskstart)
    attachment.title.forEach { addTextContent(it) }
    text(Language.Bokmal to bokmalend, Language.Nynorsk to nynorskend)
}

@JvmName("namedReferenceBokmalEnglish")
fun TextScope<LangBokmalEnglish, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Language.Bokmal to bokmalstart, Language.English to englishstart)
    attachment.title.forEach { addTextContent(it as TextElement<LangBokmalEnglish>) }
    text(Language.Bokmal to bokmalend, Language.English to englishend)
}

@JvmName("namedReferenceBokmal")
fun TextScope<LangBokmal, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Language.Bokmal to bokmalstart)
    attachment.title.forEach { addTextContent(it as TextElement<LangBokmal>) }
    text(Language.Bokmal to bokmalend)
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