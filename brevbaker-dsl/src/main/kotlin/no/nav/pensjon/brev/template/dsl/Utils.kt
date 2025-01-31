package no.nav.pensjon.brev.template.dsl

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

fun <Lang1 : Language> newTextExpr(
    lang1: Pair<Lang1, StringExpression>,
    fontType: FontType = PLAIN,
): TextElement<LanguageSupport.Single<Lang1>> =
    Content(Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, fontType))

fun <Lang1 : Language, Lang2 : Language> newTextExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    fontType: FontType = PLAIN,
): TextElement<LanguageSupport.Double<Lang1, Lang2>> =
    Content(Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, lang2, fontType))

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> newTextExpr(
    lang1: Pair<Lang1, StringExpression>,
    lang2: Pair<Lang2, StringExpression>,
    lang3: Pair<Lang3, StringExpression>,
    fontType: FontType = PLAIN,
): TextElement<LanguageSupport.Triple<Lang1, Lang2, Lang3>> =
    Content(Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(lang1, lang2, lang3, fontType))

fun <Lang1 : Language> languages(lang1: Lang1): LanguageSupport.Single<Lang1> =
    LanguageCombination.Single(lang1)

fun <Lang1 : Language, Lang2 : Language> languages(lang1: Lang1, lang2: Lang2): LanguageSupport.Double<Lang1, Lang2> =
    LanguageCombination.Double(lang1, lang2)

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> languages(lang1: Lang1, lang2: Lang2, lang3: Lang3): LanguageSupport.Triple<Lang1, Lang2, Lang3> =
    LanguageCombination.Triple(lang1, lang2, lang3)
