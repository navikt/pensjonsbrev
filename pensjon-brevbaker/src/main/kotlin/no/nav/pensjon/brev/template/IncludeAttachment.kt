package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.text


fun <Lang : LanguageSupport, LetterData : Any> createAttachment(
    title: TextElement<Lang>,
    includeSakspart: Boolean = false,
    outline: OutlineOnlyScope<Lang, LetterData>.() -> Unit
) = AttachmentTemplate<Lang, LetterData>(
    title,
    OutlineOnlyScope<Lang, LetterData>().apply(outline).elements,
    includeSakspart
)

fun TextScope<BaseLanguages, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    text(Language.Bokmal to "«", Language.Nynorsk to "«", Language.English to "“")
    addTextContent(attachment.title)
    text(Language.Bokmal to "»", Language.Nynorsk to "»", Language.English to "”")
}