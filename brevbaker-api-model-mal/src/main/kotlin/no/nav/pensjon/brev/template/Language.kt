package no.nav.pensjon.brev.template

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