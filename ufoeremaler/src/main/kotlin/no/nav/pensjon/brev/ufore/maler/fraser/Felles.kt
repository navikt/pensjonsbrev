package no.nav.pensjon.brev.ufore.maler.fraser

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class Felles {

    object HvaSkjerNa : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Hva skjer nå?" },
                )
            }
            paragraph {
                text(bokmal { +
                    "Har du fått arbeidsavklaringspenger, får du ikke lenger disse når vi avslår søknaden din om uføretrygd. " +
                    "Kontakt din veileder på ditt lokale Nav-kontor for å høre om du trenger mer avklaring, eller om det er andre pengestøtter fra Nav som kan være aktuelle for deg. " +
                    "Din veileder har fått beskjed om at vi har avslått søknaden din. " })
            }
        }
    }

    object RettTilAKlageLang : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Hvis du mener vedtaket er feil, kan du klage innen 6 uker fra den datoen vedtaket har kommet fram til deg. Dette følger av folketrygdloven § 21-12. " +
                    "Du finner skjema og informasjon på ${Constants.KLAGE_URL}."},
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Nav kan veilede deg på telefon om hvordan du sender en klage. Nav-kontoret ditt kan også hjelpe deg med å skrive en klage. " +
                    "Kontakt oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} hvis du trenger hjelp."},
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Får du medhold i klagen, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket, for eksempel hjelp fra advokat. " +
                    "Du kan ha krav på fri rettshjelp etter rettshjelploven. Du kan få mer informasjon om denne ordningen hos advokater, statsforvalteren eller Nav."},
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Du kan lese om saksomkostninger i forvaltningsloven § 36."},
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Hvis du sender klage i posten, må du signere klagen."},
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Mer informasjon om klagerettigheter finner du på ${Constants.KLAGERETTIGHETER_URL}."},
                )
            }
        }
    }

    object RettTilAKlageKort : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Hvis du mener vedtaket er feil, kan du klage innen 6 uker fra den datoen vedtaket har kommet fram til deg. Dette følger av folketrygdloven § 21-12. " +
                            "Du finner skjema og informasjon på ${Constants.KLAGE_URL}."},
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Nav kan veilede deg på telefon om hvordan du sender en klage. Nav-kontoret ditt kan også hjelpe deg med å skrive en klage. " +
                            "Kontakt oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} hvis du trenger hjelp."},
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Hvis du sender klage i posten, må du signere klagen."},
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Mer informasjon om klagerettigheter finner du på ${Constants.KLAGERETTIGHETER_URL}."},
                )
            }
        }
    }

    object RettTilInnsynRefVedlegg : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram."}
                )
            }
        }
    }

    object RettTilInnsyn : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å se dokumentene i saken din. Du kan logge deg inn via nav.no for å se dokumenter i saken din."}
                )
            }
        }
    }

    object HarDuSporsmal : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Har du spørsmål?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du finner mer informasjon på ${Constants.UFORE_URL}. " +
                            "På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. " +
                            "Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} " +
                            "hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                    }
                )
            }
        }
    }
}