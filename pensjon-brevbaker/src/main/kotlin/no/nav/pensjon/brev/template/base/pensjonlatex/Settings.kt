package no.nav.pensjon.brev.template.base.pensjonlatex

import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.api.model.NAVEnhet
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

val pensjonLatexSettings = languageSettings {
    setting("navnprefix") {
        text(
            Language.Bokmal to "Navn:",
            Language.Nynorsk to "Namn:",
            Language.English to "Name:",
        )
    }

    setting("saksnummerprefix") {
        text(
            Language.Bokmal to "Saksnummer:",
            Language.Nynorsk to "Saksnummer:",
            Language.English to "Case number:",
        )
    }

    setting("foedselsnummerprefix") {
        text(
            Language.Bokmal to "Fødselsnummer:",
            Language.Nynorsk to "Fødselsnummer:",
            Language.English to "National identity number:",
        )
    }

    setting("sideprefix") {
        text(
            Language.Bokmal to "side",
            Language.Nynorsk to "side",
            Language.English to "page",
        )
    }

    setting("sideinfix") {
        text(
            Language.Bokmal to "av",
            Language.Nynorsk to "av",
            Language.English to "of",
        )
    }

    setting("closingspoersmaal") {
        text(
            Language.Bokmal to "Har du spørsmål?",
            Language.Nynorsk to "Har du spørsmål?",
            Language.English to "Do you have questions?",
        )
    }

    setting("closingkontaktoss") {
        val avsender = felles().select(Felles::avsenderEnhet)
        val nettside = avsender.select(NAVEnhet::nettside)
        val telefonnummer = avsender.select(NAVEnhet::telefonnummer).format()

        textExpr(
            Language.Bokmal to "Kontakt oss gjerne på ".expr() + nettside + " eller på telefon " + telefonnummer
                    + ". Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask og god hjelp.",
            Language.Nynorsk to "Kontakt oss gjerne på ".expr() + nettside + " eller på telefon " + telefonnummer
                    + ". Dersom du gir opp fødselsnummeret ditt når du kontaktar NAV, kan vi lettare gi deg rask og god hjelp.",
            Language.English to "You will find further information at ".expr() + nettside
                    + ". You can also contact us by phone " + telefonnummer + ".",
        )
    }

    setting("closinggreeting") {
        text(
            Language.Bokmal to "Med vennlig hilsen",
            Language.Nynorsk to "Med vennleg helsing",
            Language.English to "Yours sincerely",
        )
    }

    setting("closingsaksbehandlersuffix") {
        text(
            Language.Bokmal to "saksbehandler",
            Language.Nynorsk to "saksbehandlar",
            Language.English to "Executive Officer",
        )
    }
    setting("closingautomatisktext") {
        text(
            Language.Bokmal to "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
            Language.Nynorsk to "Brevet er produsert automatisk og er difor ikkje underskrive av saksbehandler.",
            Language.English to "This letter has been processed automatically and is therefore not signed by an assessor.",
        )
    }
    setting("closingvedleggprefix") {
        text(
            Language.Bokmal to "Vedlegg:",
            Language.Nynorsk to "Vedlegg:",
            Language.English to "Attachments:",
        )
    }
    setting("tablenextpagecontinuation") {
        text(
            Language.Bokmal to "Fortsettelse på neste side",
            Language.Nynorsk to "Fortsettelse på neste side",
            Language.English to "Continued on the next page",
        )
    }
    setting("tablecontinuedfrompreviouspage") {
        text(
            Language.Bokmal to "Fortsettelse fra forrige side",
            Language.Nynorsk to "Fortsettelse fra forrige side",
            Language.English to "Continuation from previous page",
        )
    }
}