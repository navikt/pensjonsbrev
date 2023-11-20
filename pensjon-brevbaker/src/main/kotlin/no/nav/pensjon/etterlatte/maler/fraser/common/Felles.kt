package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object Felles {

    object VeiledningFraNavForvaltningsloven11 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Veiledning fra NAV - forvaltningsloven § 11",
                    Nynorsk to "Rettleiing frå NAV – forvaltingslova § 11",
                    English to "Guidance from NAV – Section 11 of the Public Administration Act",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi har plikt til å veilede deg om dine rettigheter og plikter i saken din, både før, " +
                            "under og etter saksbehandlingen. Dersom du har spørsmål eller er usikker på noe, vil vi gjøre " +
                            "vårt beste for å hjelpe deg.",
                    Nynorsk to "Vi pliktar å rettleie deg om rettane og pliktene du har i saka, både før, " +
                            "under og etter saksbehandlinga. Dersom du har spørsmål eller er usikker på noko, " +
                            "vil vi gjere vårt beste for å hjelpe deg.",
                    English to "We have a duty to advise you of your rights and obligations in your case – before, " +
                            "during and after the case has been processed. " +
                            "If you have any questions or are unsure about anything, we will do our best to help you.",
                )
            }
        }
    }

    object HjelpFraAndreForvaltningsloven12 : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Hjelp fra andre - forvaltningsloven § 12",
                    Nynorsk to "Hjelp frå andre – forvaltingslova § 12",
                    English to "Help from others – Section 12 of the Public Administration Act",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel av advokat, " +
                            "rettshjelper, en organisasjon du er medlem av eller en annen myndig person. Hvis den som " +
                            "hjelper deg ikke er advokat, må du gi denne personen en skriftlig fullmakt. Bruk gjerne " +
                            "skjemaet du finner på ${Constants.FULLMAKT_URL}.",
                    Nynorsk to "Du har under heile saksbehandlinga høve til å be om hjelp frå til dømes advokat, " +
                            "rettshjelpar, organisasjonar du er medlem av, eller andre myndige personar. " +
                            "Dersom personen som hjelper deg, ikkje er advokat, " +
                            "må du gi vedkomande ei skriftleg fullmakt. " +
                            "Bruk gjerne skjemaet du finn på ${Constants.FULLMAKT_URL}.",
                    English to "You can ask for help from others throughout case processing, such as an attorney, " +
                            "legal aide, an organization of which you are a member or another person of legal age. " +
                            "If the person helping you is not an attorney, " +
                            "you must give this person a written power of attorney. " +
                            "Feel free to use the form you find here:  ${Constants.Engelsk.FULLMAKT_URL}.",
                )
            }
        }
    }

}