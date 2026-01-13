package no.nav.brev.brevbaker

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.ContentOrControlStructure.Content
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN

fun <Lang1 : Language> newText(
    lang1: Pair<Lang1, String>,
    fontType: FontType = PLAIN,
): TextElement<LanguageSupport.Single<Lang1>> =
    Content(Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, fontType))

fun <Lang1 : Language, Lang2 : Language> newText(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    fontType: FontType = PLAIN,
): TextElement<LanguageSupport.Double<Lang1, Lang2>> =
    Content(Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, fontType))

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> newText(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
    fontType: FontType = PLAIN,
): TextElement<LanguageSupport.Triple<Lang1, Lang2, Lang3>> =
    Content(Element.OutlineContent.ParagraphContent.Text.Literal.create(lang1, lang2, lang3, fontType))