package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brev.api.model.NAVEnhetSelectors.nettside
import no.nav.pensjon.brev.api.model.NAVEnhetSelectors.telefonnummer
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

object LanguageSetting {
    object Sakspart {
        const val navn = "navnprefix"
        const val saksnummer = "saksnummerprefix"
        const val foedselsnummer = "foedselsnummerprefix"
    }
    object Closing {
        const val harDuSpoersmaal = "closingspoersmaal"
        const val kontaktOss = "closingkontaktoss"
        const val greeting = "closinggreeting"
        const val saksbehandler = "closingsaksbehandlersuffix"
        const val automatisk = "closingautomatisktext"
    }
}

val pensjonLatexSettings = languageSettings {
    setting(LanguageSetting.Sakspart.navn) {
        text(
            Language.Bokmal to "Navn:",
            Language.Nynorsk to "Namn:",
            Language.English to "Name:",
        )
    }

    setting(LanguageSetting.Sakspart.saksnummer) {
        text(
            Language.Bokmal to "Saksnummer:",
            Language.Nynorsk to "Saksnummer:",
            Language.English to "Case number:",
        )
    }

    setting(LanguageSetting.Sakspart.foedselsnummer) {
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

    // TODO: Slå sammen closingspoersmaal og closingkontaktoss til en frase.
    setting(LanguageSetting.Closing.harDuSpoersmaal) {
        text(
            Language.Bokmal to "Har du spørsmål?",
            Language.Nynorsk to "Har du spørsmål?",
            Language.English to "Do you have questions?",
        )
    }

    setting(LanguageSetting.Closing.kontaktOss) {
        val nettside = felles.avsenderEnhet.nettside
        val telefonnummer = felles.avsenderEnhet.telefonnummer.format()

        textExpr(
            Language.Bokmal to "Kontakt oss gjerne på ".expr() + nettside + " eller på telefon " + telefonnummer
                    + ". Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask og god hjelp.",
            Language.Nynorsk to "Kontakt oss gjerne på ".expr() + nettside + " eller på telefon " + telefonnummer
                    + ". Dersom du gir opp fødselsnummeret ditt når du kontaktar NAV, kan vi lettare gi deg rask og god hjelp.",
            Language.English to "You will find further information at ".expr() + nettside
                    + ". You can also contact us by phone " + telefonnummer + ".",
        )
    }

    setting(LanguageSetting.Closing.greeting) {
        text(
            Language.Bokmal to "Med vennlig hilsen",
            Language.Nynorsk to "Med vennleg helsing",
            Language.English to "Yours sincerely",
        )
    }

    setting(LanguageSetting.Closing.saksbehandler) {
        text(
            Language.Bokmal to "Saksbehandler",
            Language.Nynorsk to "Saksbehandlar",
            Language.English to "Executive Officer",
        )
    }
    setting(LanguageSetting.Closing.automatisk) {
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

val pensjonHTMLSettings = pensjonLatexSettings