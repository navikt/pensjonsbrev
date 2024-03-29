package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, out Any>.postadresse(
    utland: Expression<Boolean>
) {
    paragraph {
        text(
            Language.Bokmal to "NAV skanning",
            Language.Nynorsk to "NAV skanning",
            Language.English to "NAV skanning"
        )
        newline()
        text(
            Language.Bokmal to "Postboks 1400",
            Language.Nynorsk to "Postboks 1400",
            Language.English to "Postboks 1400"
        )
        newline()
        text(
            Language.Bokmal to "0109 OSLO",
            Language.Nynorsk to "0109 OSLO",
            Language.English to "0109 OSLO"
        )
        showIf(utland) {
            newline()
            text(
                Language.Bokmal to "Norge/Norway",
                Language.Nynorsk to "Noreg/Norway",
                Language.English to "Norway"
            )
        }
    }

}