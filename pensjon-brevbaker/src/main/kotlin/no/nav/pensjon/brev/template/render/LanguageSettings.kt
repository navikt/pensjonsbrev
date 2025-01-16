package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.languageSettings

object LanguageSetting {
    object Sakspart {
        const val navn = "navnprefix"
        const val gjelderNavn = "gjeldernavnprefix"
        const val vedleggGjelderNavn = "vedlegggjeldernavnprefix"
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

    object HTML {
        const val altTextLogo = "altTextLogo"
    }
}

val pensjonLatexSettings = languageSettings {
    setting(LanguageSetting.Sakspart.navn) {
        Literal.create(
            Bokmal to "Navn:",
            Nynorsk to "Namn:",
            English to "Name:",
        )
    }

    setting(LanguageSetting.Sakspart.vergenavn) {
        Literal.create(
            Bokmal to "Verge:",
            Nynorsk to "Verje:",
            English to "Guardian:",
        )
    }

    setting(LanguageSetting.Sakspart.vedleggGjelderNavn) {
        Literal.create(
            Bokmal to "Vedlegget gjelder:",
            Nynorsk to "Vedlegget gjeld:",
            English to "Attachment regarding:",
        )
    }

    setting(LanguageSetting.Sakspart.gjelderNavn) {
        Literal.create(
            Bokmal to "Saken gjelder:",
            Nynorsk to "Saka gjeld:",
            English to "Case regarding:",
        )
    }

    setting(LanguageSetting.Sakspart.saksnummer) {
        Literal.create(
            Bokmal to "Saksnummer:",
            Nynorsk to "Saksnummer:",
            English to "Case number:",
        )
    }

    setting(LanguageSetting.Sakspart.foedselsnummer) {
        Literal.create(
            Bokmal to "Fødselsnummer:",
            Nynorsk to "Fødselsnummer:",
            English to "National identity number:",
        )
    }

    setting("sidesaksnummerprefix") {
        Literal.create(
            Bokmal to "Saksnummer: ",
            Nynorsk to "Saksnummer: ",
            English to "Case number: ",
        )
    }

    setting("sideprefix") {
        Literal.create(
            Bokmal to "side",
            Nynorsk to "side",
            English to "page",
        )
    }

    setting("sideinfix") {
        Literal.create(
            Bokmal to "av",
            Nynorsk to "av",
            English to "of",
        )
    }

    setting(LanguageSetting.Closing.greeting) {
        Literal.create(
            Bokmal to "Med vennlig hilsen",
            Nynorsk to "Med vennleg helsing",
            English to "Yours sincerely",
        )
    }

    setting(LanguageSetting.Closing.saksbehandler) {
        Literal.create(
            Bokmal to "Saksbehandler",
            Nynorsk to "Saksbehandlar",
            English to "Caseworker",
        )
    }
    setting(LanguageSetting.Closing.automatiskInformasjonsbrev) {
        Literal.create(
            Bokmal to "Brevet er produsert automatisk og derfor ikke underskrevet av saksbehandler.",
            Nynorsk to "Brevet er produsert automatisk og er difor ikkje underskrive av saksbehandler.",
            English to "This letter has been processed automatically and is therefore not signed by an assessor.",
        )
    }
    setting(LanguageSetting.Closing.automatiskVedtaksbrev) {
        Literal.create(
            Bokmal to "Saken har blitt automatisk saksbehandlet. Vedtaksbrevet er derfor ikke underskrevet av saksbehandler.",
            Nynorsk to "Saken har blitt automatisk saksbehandla. Vedtaksbrevet er derfor ikkje underskriven av saksbehandlar.",
            English to "Your case has been processed automatically. The decision letter has therefore not been signed by an assessor.",
        )
    }
    setting("closingvedleggprefix") {
        Literal.create(
            Bokmal to "Vedlegg:",
            Nynorsk to "Vedlegg:",
            English to "Attachments:",
        )
    }
    setting("tablenextpagecontinuation") {
        Literal.create(
            Bokmal to "Tabellen fortsetter på neste side",
            Nynorsk to "Tabellen fortsett på neste side",
            English to "Continued on next page",
        )
    }
    setting("tablecontinuedfrompreviouspage") {
        Literal.create(
            Bokmal to "Fortsettelse fra forrige side",
            Nynorsk to "Fortsetjing frå førre side",
            English to "Continuation from previous page",
        )
    }
}

val pensjonHTMLSettings = languageSettings(pensjonLatexSettings) {
    setting(LanguageSetting.HTML.altTextLogo) {
        Literal.create(
            Bokmal to "Nav logo",
            Nynorsk to "Nav logo",
            English to "Nav logo",
        )
    }
}