package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.dsl.QuotationMarks
import no.nav.pensjon.brev.template.dsl.TextScope
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN

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

private fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> newText(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = PLAIN,
): TextElement<LanguageSupport.Triple<Lang1, Lang2, Lang3>> =
    Content(Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, lang3, fontType))