package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageCombination
import no.nav.pensjon.brev.template.LanguageSupport

fun <Lang1 : Language> newText(lang1: Pair<Lang1, String>): Element.Text.Literal<LanguageSupport.Single<Lang1>> =
    Element.Text.Literal.create(lang1)

fun <Lang1 : Language, Lang2 : Language> newText(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
): Element.Text.Literal<LanguageSupport.Double<Lang1, Lang2>> =
    Element.Text.Literal.create(lang1, lang2)

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> newText(
    lang1: Pair<Lang1, String>,
    lang2: Pair<Lang2, String>,
    lang3: Pair<Lang3, String>,
): Element.Text.Literal<LanguageSupport.Triple<Lang1, Lang2, Lang3>> =
    Element.Text.Literal.create(lang1, lang2, lang3)


fun <Lang1 : Language> languages(lang1: Lang1) =
    LanguageCombination.Single(lang1)

fun <Lang1 : Language, Lang2 : Language> languages(lang1: Lang1, lang2: Lang2) =
    LanguageCombination.Double(lang1, lang2)

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> languages(lang1: Lang1, lang2: Lang2, lang3: Lang3) =
    LanguageCombination.Triple(lang1, lang2, lang3)
