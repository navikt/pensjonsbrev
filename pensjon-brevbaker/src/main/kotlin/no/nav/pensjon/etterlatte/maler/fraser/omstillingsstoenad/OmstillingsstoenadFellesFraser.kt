package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

object OmstillingsstoenadFellesFraser {

    object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must report any changes"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har plikt til å melde fra til oss om endringer som har betydning for " +
                            "utbetalingen av omstillingsstønaden din, eller retten til å få omstillingsstønad. " +
                            "I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Nynorsk to "Du pliktar å melde frå til oss om endringar som har innverknad på utbetalinga " +
                            "av eller retten på omstillingsstønad. I vedlegget «Dine rettar og plikter» ser du kva " +
                            "endringar du må seie frå om. ",
                    English to "You are obligated to notify us of any changes that affect the payment of " +
                            "transitional benefits, or the right to receive transitional benefits. You will see " +
                            "which changes you must report in the attachment, Your Rights and Obligations."
                )
            }
        }
    }

    object DuHarRettTilAaKlage  : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage",
                    English to "You have the right to appeal"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema " +
                            "og informasjon på ${Constants.KLAGE_URL}.",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den " +
                            "datoen du fekk vedtaket. Klaga må vere skriftleg. Du finn skjema og informasjon " +
                            "på ${Constants.KLAGE_URL}.",
                    English to "If you believe the decision is incorrect, you may appeal the decision within " +
                            "six weeks from the date you received the decision. The appeal must be in writing. " +
                            "You can find the form and information online: ${Constants.Engelsk.KLAGE_URL}."
                )
            }
        }
    }

    object DuHarRettTilAaKlageAvslagOpphoer  : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "Du har rett til å klage",
                    English to "You have the right to appeal"
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Du kan lese mer om hvordan du klager i " +
                            "vedlegget «Informasjon om klage og anke».",
                    Nynorsk to "Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den " +
                            "datoen du fekk vedtaket. I vedlegget «Informasjon om klage og anke» kan du lese " +
                            "meir om korleis du klagar.",
                    English to "If you believe the decision is incorrect, you may appeal the decision within " +
                            "six weeks from the date you received the decision. You can read more about how to " +
                            "appeal in the attachment Information on Complaints and Appeals.  "
                )
            }
        }
    }

    object DuHarRettTilInnsyn : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "Du har rett til innsyn",
                    English to "You have the right to access documents"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har som hovedregel rett til å se dokumentene i saken din " +
                            "etter bestemmelsene i forvaltningsloven § 18. Hvis du ønsker innsyn, " +
                            "må du kontakte oss på telefon eller per post.",
                    Nynorsk to "Etter føresegnene i forvaltingslova § 18 har du som hovudregel rett til å " +
                            "sjå dokumenta i saka di. Kontakt oss på telefon eller per post dersom du ønskjer innsyn.",
                    English to "As a general rule, you have the right to see the documents in your case " +
                            "pursuant to the provisions of Section 18 of the Public Administration Act. " +
                            "If you want access, you can contact us by phone or mail. "
                )
            }
        }
    }

    object HarDuSpoersmaal : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Har du spørsmål?",
                    Nynorsk to "Har du spørsmål?",
                    English to "Any questions?"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan finne svar på ${Constants.OMS_URL}. På ${Constants.KONTAKT_URL} kan du " +
                            "chatte eller skrive til oss. Du kan også kontakte oss på telefon " +
                            "+47 ${Constants.KONTAKTTELEFON_PENSJON}, hverdager 09.00-15.00. Hvis du oppgir " +
                            "fødselsnummer, kan vi lettere gi deg rask og god hjelp.",
                    Nynorsk to "Du kan finne svar på ${Constants.OMS_URL}. Du kan skrive til eller chatte " +
                            "med oss på ${Constants.KONTAKT_URL}. Du kan også kontakte oss på telefon " +
                            "+47 ${Constants.KONTAKTTELEFON_PENSJON}, kvardagar 09:00–15:00. Det vil gjere det " +
                            "enklare for oss å gi deg rask og god hjelp om du oppgir fødselsnummer.",
                    English to "You can find answers to your questions online: ${Constants.OMS_URL}. " +
                            "Feel free to chat with us or write to us here: ${Constants.KONTAKT_URL}. You can also contact " +
                            "us by phone (+47 ${Constants.KONTAKTTELEFON_PENSJON}), weekdays 09:00-15:00. If you " +
                            "provide your national identity number, we can more easily provide you with quick " +
                            "and good help. "
                )
            }
        }
    }
}