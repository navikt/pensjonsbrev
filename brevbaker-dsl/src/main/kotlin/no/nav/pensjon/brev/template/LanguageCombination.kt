package no.nav.pensjon.brev.template

import java.util.Objects

internal sealed class LanguageCombination {

    class Single<Lang : Language>(val first: Lang) : LanguageCombination(), LanguageSupport.Single<Lang>, StableHash by StableHash.of(first) {
        override fun supports(language: Language): Boolean = language == first
        override fun all(): Set<Language> = setOf(first)

        override fun equals(other: Any?): Boolean {
            if (other !is Single<*>) return false
            return first == other.first
        }
        override fun hashCode(): Int = Objects.hash(first)
        override fun toString() = "Single(first=$first)"

    }

    class Double<Lang1 : Language, Lang2 : Language>(
        val first: Lang1,
        val second: Lang2,
    ) : LanguageCombination(), LanguageSupport.Double<Lang1, Lang2>, StableHash by StableHash.of(first, second) {
        override fun supports(language: Language): Boolean = language == first || language == second
        override fun all(): Set<Language> = setOf(first, second)

        override fun equals(other: Any?): Boolean {
            if (other !is Double<*, *>) return false
            return first == other.first && second == other.second
        }
        override fun hashCode(): Int = Objects.hash(first, second)
        override fun toString() = "Double(first=$first, second=$second)"

    }

    class Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language>(
        val first: Lang1,
        val second: Lang2,
        val third: Lang3
    ) : LanguageCombination(), LanguageSupport.Triple<Lang1, Lang2, Lang3>, StableHash by StableHash.of(first, second, third) {
        override fun supports(language: Language): Boolean =
            language == first || language == second || language == third
        override fun all(): Set<Language> = setOf(first, second, third)

        override fun equals(other: Any?): Boolean {
            if (other !is Triple<*, *, *>) return false
            return first == other.first && second == other.second && third == other.third
        }
        override fun hashCode(): Int = Objects.hash(first, second, third)
        override fun toString() = "Triple(first=$first, second=$second, third=$third)"

    }

}