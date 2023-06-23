package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object OMSFelles {

        object MeldFraOmEndringer : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Meld fra om endringer",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis det skjer endringer som kan ha betydning for stønaden din, må " +
                            "du si ifra til oss. Vi vil ha melding dersom du"
                )
                list {
                    item { text(Language.Bokmal to "gifter deg eller blir samboer med en du tidligere har vært gift med") }
                    item { text(Language.Bokmal to "blir samboer med en du har eller har hatt felles barn med") }
                    item { text(Language.Bokmal to "får felles barn med en ny samboer") }
                    item { text(Language.Bokmal to "flytter eller oppholder deg i et annet land i 6 måneder eller mer") }
                    item { text(Language.Bokmal to "får varig opphold i institusjon") }
                }
                text(
                    Language.Bokmal to "Du sier ifra om endringer ved å skrive en beskjed til oss på " +
                            "${Constants.SKRIVTILOSS_URL} eller ved å sende brev til oss på " +
                            "NAV Familie- og pensjonsytelser, postboks 6600 Etterstad, 0607 OSLO."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du er ansvarlig for å holde deg orientert om utbetaling av " +
                            "omstillingsstønaden, og du må straks melde fra om eventuelle feil til NAV. " +
                            "Du kan lese mer om endringer på ${Constants.OMS_ENDRING_URL}."
                )
            }
        }
    }

    object DuHarRettTilAaKlage  : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner skjema " +
                            "og informasjon på ${Constants.OMS_KLAGE_URL}."
                )
            }
        }
    }

    object DuHarRettTilAaKlageOpphoer  : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker " +
                            "fra den datoen du mottok vedtaket. Klagen skal være skriftlig. Du finner " +
                            "informasjon på ${Constants.KLAGERETTIGHETER_URL}."
                )
            }
        }
    }

    object DuHarRettTilInnsyn : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Du har rett til innsyn",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du har som hovedregel rett til å se dokumentene i saken din " +
                            "etter bestemmelsene i forvaltningsloven § 18. Hvis du ønsker innsyn, " +
                            "må du kontakte oss på telefon eller per post."
                )
            }
        }
    }

    object HarDuSpoersmaal : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan finne svar på ${Constants.OMS_URL}."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan også kontakte oss på telefon 55 55 33 34, hverdager 09.00-15.00. " +
                            "Hvis du oppgir fødselsnummer, kan vi lettere gi deg rask og god hjelp."
                )
            }
        }
    }

}