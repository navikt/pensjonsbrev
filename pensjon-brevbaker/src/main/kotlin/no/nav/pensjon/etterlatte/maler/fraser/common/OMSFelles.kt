package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object OMSFelles {

        object MeldFraOmEndringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har plikt til å melde fra til oss om endringer som har betydning for " +
                            "utbetalingen av omstillingsstønaden din, eller retten til å få omstillingsstønad. " +
                            "I vedlegget «Dine rettigheter og plikter» ser du hvilke endringer du må si fra om.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }

    object DuHarRettTilAaKlage  : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema " +
                            "og informasjon på ${Constants.KLAGE_URL}.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }

    object DuHarRettTilAaKlageOpphoer  : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til å klage",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner " +
                            "informasjon på ${Constants.KLAGERETTIGHETER_URL}.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }

    object DuHarRettTilInnsyn : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du har rett til innsyn",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har som hovedregel rett til å se dokumentene i saken din " +
                            "etter bestemmelsene i forvaltningsloven § 18. Hvis du ønsker innsyn, " +
                            "må du kontakte oss på telefon eller per post.",
                    Nynorsk to "",
                    English to ""
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
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan finne svar på ${Constants.OMS_URL}. På ${Constants.KONTAKT_URL} kan du " +
                            "chatte eller skrive til oss.",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                text(
                    Bokmal to "Du kan også kontakte oss på telefon ${Constants.KONTAKTTELEFON}, hverdager 09.00-15.00. " +
                            "Hvis du oppgir fødselsnummer, kan vi lettere gi deg rask og god hjelp.",
                    Nynorsk to "",
                    English to ""
                )
            }
        }
    }

}