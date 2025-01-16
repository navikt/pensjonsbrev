package no.nav.pensjon.brev.template

sealed class LanguageCombination {

    data class Single<Lang : Language>(val first: Lang) : LanguageCombination(), LanguageSupport.Single<Lang>, StableHash by StableHash.of(first) {
        override fun supports(language: Language): Boolean = language == first
        override fun all(): Set<Language> = setOf(first)
    }

    data class Double<Lang1 : Language, Lang2 : Language>(
        val first: Lang1,
        val second: Lang2,
    ) : LanguageCombination(), LanguageSupport.Double<Lang1, Lang2>, StableHash by StableHash.of(first, second) {
        override fun supports(language: Language): Boolean = language == first || language == second
        override fun all(): Set<Language> = setOf(first, second)
    }

    data class Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language>(
        val first: Lang1,
        val second: Lang2,
        val third: Lang3
    ) : LanguageCombination(), LanguageSupport.Triple<Lang1, Lang2, Lang3>, StableHash by StableHash.of(first, second, third) {
        override fun supports(language: Language): Boolean =
            language == first || language == second || language == third
        override fun all(): Set<Language> = setOf(first, second, third)
    }
}