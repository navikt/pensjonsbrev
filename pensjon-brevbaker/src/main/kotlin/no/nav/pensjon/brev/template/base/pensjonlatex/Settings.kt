package no.nav.pensjon.brev.template.base.pensjonlatex

import no.nav.pensjon.brev.api.model.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.select

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
            Language.Bokmal to "NAVs saksnummer:",
            Language.Nynorsk to "NAVs saksnummer:",
            Language.English to "NAV’s case number:",
        )
    }

    setting("foedselsnummerprefix") {
        text(
            Language.Bokmal to "Fødselsnummer:",
            Language.Nynorsk to "Fødselsnummer:",
            Language.English to "National identity number:",
        )
    }

    setting("returadresseenhetprefix") {
        text(
            Language.Bokmal to "Returadresse:",
            Language.Nynorsk to "Returadresse:",
            Language.English to "Return address:",
        )
    }

    setting("datoprefix") {
        text(
            Language.Bokmal to "Dato:",
            Language.Nynorsk to "Dato:",
            Language.English to "Date:",
        )
    }

    setting("postadresseprefix") {
        text(
            Language.Bokmal to "Postadresse:",
            Language.Nynorsk to "Postadresse:",
            Language.English to "Mailing address:",
        )
    }

    setting("sideprefix") {
        text(
            Language.Bokmal to "Side",
            Language.Nynorsk to "Side",
            Language.English to "Page",
        )
    }

    setting("sideinfix") {
        text(
            Language.Bokmal to "av",
            Language.Nynorsk to "av",
            Language.English to "of",
        )
    }

    setting("navenhettlfprefix") {
        text(
            Language.Bokmal to "Telefon:",
            Language.Nynorsk to "Telefon:",
            Language.English to "Phone number:",
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
        text(
            Language.Bokmal to "Kontakt oss gjerne på ",
            Language.Nynorsk to "Kontakt oss gjerne på ",
            Language.English to "You will find further information at ",
        )
        val avsender = felles().select(Felles::avsenderEnhet)

        eval { avsender.select(NAVEnhet::nettside) }
        text(
            Language.Bokmal to " eller på telefon ",
            Language.Nynorsk to " eller på telefon ",
            Language.English to ". You can also contact us by phone ",
        )
        eval { avsender.select(NAVEnhet::telefonnummer).select(Telefonnummer::format) }
        text(
            Language.Bokmal to ". Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask og god hjelp.",
            Language.Nynorsk to ". Dersom du gir opp fødselsnummeret ditt når du kontaktar NAV, kan vi lettare gi deg rask og god hjelp.",
            Language.English to ".",
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
            Language.Bokmal to "(Fortsettelse)",
            Language.Nynorsk to "(Fortsettelse)",
            Language.English to "(Continuation)",
        )
    }
}