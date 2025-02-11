package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import java.util.Locale

sealed class Language : StableHash {
    val name: String = this::class.java.name

    override fun toString(): String {
        return this::class.qualifiedName!!
    }

    fun locale(): Locale =
        when (this) {
            Bokmal -> Locale.forLanguageTag("no")
            Nynorsk -> Locale.forLanguageTag("no")
            English -> Locale.UK
        }

    object Bokmal : Language(), StableHash by StableHash.of("Language.Bokmal")
    object Nynorsk : Language(), StableHash by StableHash.of("Language.Nynorsk")
    object English : Language(), StableHash by StableHash.of("Language.English")
}

fun <Lang1 : Language, Lang2 : Language, LetterData : Any> OutlineOnlyScope<LanguageSupport.Double<Lang1, Lang2>, LetterData>.includePhrase(phrase: OutlinePhrase<out LanguageSupport.Triple<Lang1, *, Lang2>>) {
    // Det er trygt å caste her fordi receiver og phrase begge har Lang1 og Lang2.
    @Suppress("UNCHECKED_CAST")
    (phrase as OutlinePhrase<LanguageSupport.Double<Lang1, Lang2>>).apply(this)
}