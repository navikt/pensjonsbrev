package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE

fun ParagraphOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, out Any>.kontakttelefonPensjon(utland: Expression<Boolean>) =
    showIf(utland) {
        text(
            Language.Bokmal to KONTAKTTELEFON_PENSJON_MED_LANDKODE,
            Language.Nynorsk to KONTAKTTELEFON_PENSJON_MED_LANDKODE,
            Language.English to KONTAKTTELEFON_PENSJON_MED_LANDKODE
        )
    } orShow {
        text(
            Language.Bokmal to Constants.KONTAKTTELEFON_PENSJON,
            Language.Nynorsk to Constants.KONTAKTTELEFON_PENSJON,
            Language.English to Constants.KONTAKTTELEFON_PENSJON
        )
    }