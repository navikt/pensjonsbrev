package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextScope
import no.nav.pensjon.brev.template.dsl.text

fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
    title: TextElement<Lang>,
    includeSakspart: Boolean = false,
    outline: OutlineOnlyScope<Lang, LetterData>.() -> Unit
): AttachmentTemplate<Lang, LetterData> = AttachmentTemplateImpl(
    title,
    OutlineOnlyScope<Lang, LetterData>().apply(outline).elements,
    includeSakspart
)

fun TextScope<BaseLanguages, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Language.Bokmal to "«", Language.Nynorsk to "«", Language.English to "“")
    addTextContent(attachment.title)
    text(Language.Bokmal to "»", Language.Nynorsk to "»", Language.English to "”")
}

interface IncludeAttachment<out Lang : LanguageSupport, AttachmentData : Any> : StableHash {
    val data: Expression<AttachmentData>
    val template: AttachmentTemplate<Lang, AttachmentData>
    val predicate: Expression<Boolean>
}

interface AttachmentTemplate<out Lang : LanguageSupport, AttachmentData : Any> : HasModel<AttachmentData>, StableHash {
    val title: TextElement<Lang>
    val outline: List<OutlineElement<Lang>>
    val includeSakspart: Boolean
}