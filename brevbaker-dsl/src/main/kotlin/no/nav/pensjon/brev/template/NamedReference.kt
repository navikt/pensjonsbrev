package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.QuotationMarks
import no.nav.pensjon.brev.template.dsl.TextScope
import no.nav.pensjon.brev.template.dsl.newText

private val startQuote: TextElement<BaseLanguages> = newText(
    Language.Bokmal to QuotationMarks.BokmalNynorsk.start,
    Language.Nynorsk to QuotationMarks.BokmalNynorsk.start,
    Language.English to QuotationMarks.English.start,
)
private val endQuote: TextElement<BaseLanguages> = newText(
    Language.Bokmal to QuotationMarks.BokmalNynorsk.end,
    Language.Nynorsk to QuotationMarks.BokmalNynorsk.end,
    Language.English to QuotationMarks.English.end,
)

fun <Lang : LanguageSupport> TextScope<in Lang, *>.namedReference(attachment: AttachmentTemplate<Lang, *>) {
    addTextContentBaseLanguages(startQuote)
    attachment.title.forEach { addTextContent(it) }
    addTextContentBaseLanguages(endQuote)
}

@JvmName("namedReferenceBokmalEnglish")
fun TextScope<LangBokmalEnglish, *>.namedReference(attachment: AttachmentTemplate<BaseLanguages, *>) {
    addTextContentBaseLanguages(startQuote)
    attachment.title.forEach {
        // Safe because we know that a template that support BaseLanguages will support Bokmal and English
        @Suppress("UNCHECKED_CAST")
        addTextContent(it as TextElement<LangBokmalEnglish>)
    }
    addTextContentBaseLanguages(endQuote)
}
