package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, out Any>.postadresse(
    utland: Expression<Boolean>,
) {
    paragraph {
        showIf(utland) {
            text(
                Language.Bokmal to "Nav familie- og pensjonsytelser",
                Language.Nynorsk to "Nav familie- og pensjonsytelser",
                Language.English to "Nav familie- og pensjonsytelser",
            )
            newline()
            text(
                Language.Bokmal to "Postboks 6600 Etterstad",
                Language.Nynorsk to "Postboks 6600 Etterstad",
                Language.English to "Postboks 6600 Etterstad",
            )
            newline()
            text(
                Language.Bokmal to "0607 Oslo",
                Language.Nynorsk to "0607 Oslo",
                Language.English to "0607 Oslo",
            )
            newline()
            text(
                Language.Bokmal to "Norge/Norway",
                Language.Nynorsk to "Noreg/Norway",
                Language.English to "Norway",
            )
        }.orShow {
            text(
                Language.Bokmal to "Nav skanning",
                Language.Nynorsk to "Nav skanning",
                Language.English to "Nav skanning",
            )
            newline()
            text(
                Language.Bokmal to "Postboks 1400",
                Language.Nynorsk to "Postboks 1400",
                Language.English to "Postboks 1400",
            )
            newline()
            text(
                Language.Bokmal to "0109 OSLO",
                Language.Nynorsk to "0109 OSLO",
                Language.English to "0109 OSLO",
            )
        }
    }
}
