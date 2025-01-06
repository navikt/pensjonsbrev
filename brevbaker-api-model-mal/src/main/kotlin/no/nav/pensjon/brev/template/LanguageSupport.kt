package no.nav.pensjon.brev.template

interface LanguageSupport : StableHash {
    fun supports(language: Language): Boolean
    fun all(): Set<Language>

    interface Single<Lang1 : Language> : LanguageSupport
    interface Double<Lang1 : Language, Lang2 : Language> : Single<Lang1>
    interface Triple<Lang1 : Language, Lang2 : Language, Lang3 : Language> : Double<Lang1, Lang2>
}