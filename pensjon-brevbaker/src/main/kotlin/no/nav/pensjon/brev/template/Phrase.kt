package no.nav.pensjon.brev.template

// TODO: We might not need Phrase, unless we need to store them in a database
sealed class Phrase<Lang : LanguageCombination> {
    val type: String = this::class.java.name

    abstract fun text(language: Language): String

    data class Static<Lang : LanguageCombination> private constructor(val name: String, private val text: Map<Language, String>) : Phrase<Lang>() {

        override fun text(language: Language): String =
            text[language] ?: throw IllegalArgumentException("Phrase doesn't contain language: ${language::class.qualifiedName}")

        companion object {
            fun <Lang1 : Language> create(name: String, lang1: Pair<Lang1, String>) =
                Static<LanguageCombination.Single<Lang1>>(name, mapOf(lang1))

            fun <Lang1 : Language, Lang2 : Language> create(
                name: String,
                lang1: Pair<Lang1, String>,
                lang2: Pair<Lang2, String>,
            ) = Static<LanguageCombination.Double<Lang1, Lang2>>(name, mapOf(lang1, lang2))

            fun <Lang1 : Language, Lang2 : Language, Lang3 : Language> create(
                name: String,
                lang1: Pair<Lang1, String>,
                lang2: Pair<Lang2, String>,
                lang3: Pair<Lang3, String>,
            ) = Static<LanguageCombination.Triple<Lang1, Lang2, Lang3>>(name, mapOf(lang1, lang2, lang3))
        }
    }

}
