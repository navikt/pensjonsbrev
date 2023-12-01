package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.text

fun ParagraphOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, out Any>.kontakttelefonPensjon(utland: Expression<Boolean>) =
    showIf(utland) {
        text(
            Language.Bokmal to Constants.Utland.KONTAKTTELEFON_PENSJON,
            Language.Nynorsk to Constants.Utland.KONTAKTTELEFON_PENSJON,
            Language.English to Constants.Utland.KONTAKTTELEFON_PENSJON
        )
    } orShow {
        text(
            Language.Bokmal to Constants.KONTAKTTELEFON_PENSJON,
            Language.Nynorsk to Constants.KONTAKTTELEFON_PENSJON,
            Language.English to Constants.KONTAKTTELEFON_PENSJON
        )
    }