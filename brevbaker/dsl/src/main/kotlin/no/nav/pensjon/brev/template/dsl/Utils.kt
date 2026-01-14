package no.nav.pensjon.brev.template.dsl

import no.nav.pensjon.brev.template.*

fun <Lang1 : Language> languages(lang1: Lang1): LanguageSupport.Single<Lang1> =
    LanguageCombination.Single(lang1)

fun <Lang1 : Language, Lang2 : Language> languages(lang1: Lang1, lang2: Lang2): LanguageSupport.Double<Lang1, Lang2> =
    LanguageCombination.Double(lang1, lang2)

fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> languages(lang1: Lang1, lang2: Lang2, lang3: Lang3): LanguageSupport.Triple<Lang1, Lang2, Lang3> =
    LanguageCombination.Triple(lang1, lang2, lang3)
