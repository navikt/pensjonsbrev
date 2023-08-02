package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.languageSettings
import no.nav.pensjon.brev.template.dsl.text

object LanguageSetting {
    object Sakspart {
        const val navn = "navnprefix"
        const val gjelderNavn = "gjeldernavnprefix"
        const val vergenavn = "vergenavnprefix"
        const val saksnummer = "saksnummerprefix"
        const val foedselsnummer = "foedselsnummerprefix"
    }

    object Closing {
        const val automatiskVedtaksbrev = "closingautomatisktextvedtaksbrev"
        const val greeting = "closinggreeting"
        const val saksbehandler = "closingsaksbehandlersuffix"
        const val automatiskInformasjonsbrev = "closingautomatisktextinfobrev"
    }
}

val pensjonLatexSettings = languageSettings {
    setting(LanguageSetting.Sakspart.navn) {
        text(
            Bokmal to "Navn:",
            Nynorsk to "Namn:",
            English to "Name:",
        )
    }

    setting(LanguageSetting.Sakspart.vergenavn) {
        text(
            Bokmal to "Verge:",
            Nynorsk to "Verje:",
            English to "Guardian:",
        )
    }

    setting(LanguageSetting.Sakspart.gjelderNavn) {
        text(
            Bokmal to "Saken gjelder:",
            Nynorsk to "Saka gjeld:",
            English to "Case regarding:",
        )
    }

    setting(LanguageSetting.Sakspart.saksnummer) {
        text(
            Bokmal to "Saksnummer:",
            Nynorsk to "Saksnummer:",
            English to "Case number:",
        )
    }

    setting(LanguageSetting.Sakspart.foedselsnummer) {
        text(
            Bokmal to "Fødselsnummer:",
            Nynorsk to "Fødselsnummer:",
            English to "National identity number:",
        )
    }

    setting("sidesaksnummerprefix") {
        text(
            Bokmal to "saksnummer: ",
            Nynorsk to "saksnummer: ",
            English to "case number: ",
        )
    }

    setting("sideprefix") {
        text(
            Bokmal to "side",
            Nynorsk to "side",
            English to "page",
        )
    }

    setting("sideinfix") {
        text(
            Bokmal to "av",
            Nynorsk to "av",
            English to "of",
        )
    }

    setting(LanguageSetting.Closing.greeting) {
        text(
            Bokmal to "Med vennlig hilsen",
            Nynorsk to "Med vennleg helsing",
            English to "Yours sincerely",
        )
    }

    setting(LanguageSetting.Closing.saksbehandler) {
        text(
            Bokmal to "Saksbehandler",
            Nynorsk to "Saksbehandlar",
            English to "Assessor",
        )
    }
    setting(LanguageSetting.Closing.automatiskInformasjonsbrev) {
        text(
            Bokmal to "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
            Nynorsk to "Brevet er produsert automatisk og er difor ikkje underskrive av saksbehandler.",
            English to "This letter has been processed automatically and is therefore not signed by an assessor.",
        )
    }
    setting(LanguageSetting.Closing.automatiskVedtaksbrev) {
        text(
            Bokmal to "Saken har blitt automatisk saksbehandlet. Vedtaksbrevet er derfor ikke underskrevet av saksbehandler.",
            Nynorsk to "Saken har blitt automatisk saksbehandla. Vedtaksbrevet er derfor ikkje underskriven av saksbehandlar.",
            English to "Your case has been processed automatically. The decision letter has therefore not been signed by an assessor.",
        )
    }
    setting("closingvedleggprefix") {
        text(
            Bokmal to "Vedlegg:",
            Nynorsk to "Vedlegg:",
            English to "Attachments:",
        )
    }
    setting("tablenextpagecontinuation") {
        text(
            Bokmal to "Tabellen fortsetter på neste side",
            Nynorsk to "Tabellen fortsett på neste side",
            English to "Continued on next page",
        )
    }
    setting("tablecontinuedfrompreviouspage") {
        text(
            Bokmal to "Fortsettelse fra forrige side",
            Nynorsk to "Fortsetjing frå førre side",
            English to "Continuation from previous page",
        )
    }
}

val pensjonHTMLSettings = pensjonLatexSettings