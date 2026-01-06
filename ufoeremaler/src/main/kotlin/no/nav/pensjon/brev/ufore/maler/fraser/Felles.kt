package no.nav.pensjon.brev.ufore.maler.fraser

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class Felles {

    object HvaSkjerNa : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Hva skjer nå?" },
                    nynorsk { + "Kva skjer no?" },
                )
            }
            paragraph {
                text(bokmal { +
                    "Har du fått arbeidsavklaringspenger, får du ikke lenger disse når vi avslår søknaden din om uføretrygd. " +
                    "Kontakt din veileder på ditt lokale Nav-kontor for å høre om du trenger mer avklaring, eller om det er andre pengestøtter fra Nav som kan være aktuelle for deg. " +
                    "Din veileder har fått beskjed om at vi har avslått søknaden din. " },
                    nynorsk { +
                    "Har du fått arbeidsavklaringspengar, får du ikkje lenger desse når vi avslår søknaden din om uføretrygd. " +
                    "Kontakt din rettleiar på ditt lokale Nav-kontor for å høyre om du treng meir avklaring, eller om det finst andre pengestøttar frå Nav som kan vere aktuelle for deg. " +
                    "Din rettleiar har fått beskjed om at vi har avslått søknaden din. " }
                )
            }
        }
    }

    object RettTilAKlageLang : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til å klage" },
                    nynorsk { + "Du har rett til å klage" }
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Hvis du mener vedtaket er feil, kan du klage innen 6 uker fra den datoen vedtaket har kommet fram til deg. Dette følger av folketrygdloven § 21-12. " +
                    "Du finner skjema og informasjon på ${Constants.KLAGE_URL}."},
                    nynorsk { +
                    "Dersom du meiner vedtaket er feil, kan du klage innan 6 veker frå den datoen vedtaket har kome fram til deg. Dette følgjer av folketrygdlova § 21-12. " +
                    "Du finn skjema og informasjon på ${Constants.KLAGE_URL}."}
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Nav kan veilede deg på telefon om hvordan du sender en klage. Nav-kontoret ditt kan også hjelpe deg med å skrive en klage. " +
                    "Kontakt oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} hvis du trenger hjelp."},
                    nynorsk { +
                    "Nav kan rettleie deg på telefon om korleis du sender ei klage. Nav-kontoret ditt kan òg hjelpe deg med å skrive ei klage. " +
                    "Kontakt oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} dersom du treng hjelp."}
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Får du medhold i klagen, kan du få dekket vesentlige utgifter som har vært nødvendige for å få endret vedtaket, for eksempel hjelp fra advokat. " +
                    "Du kan ha krav på fri rettshjelp etter rettshjelploven. Du kan få mer informasjon om denne ordningen hos advokater, statsforvalteren eller Nav."},
                    nynorsk { +
                    "Får du medhald i klagen, kan du få dekka vesentlege utgifter som har vore nødvendige for å få endra vedtaket, til dømes hjelp frå advokat. " +
                    "Du kan ha krav på fri rettshjelp etter rettshjelplova. Du kan få meir informasjon om denne ordninga hos advokatar, statsforvaltaren eller Nav."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan lese om saksomkostninger i forvaltningsloven § 36."},
                    nynorsk { +"Du kan lese om saksomkostnader i forvaltningslova § 36."}
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du sender klage i posten, må du signere klagen."},
                    nynorsk { +"Dersom du sender klage i posten, må du signere klagan." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Mer informasjon om klagerettigheter finner du på ${Constants.KLAGERETTIGHETER_URL}."},
                    nynorsk { +"Meir informasjon om klagerettar finn du på ${Constants.KLAGERETTIGHETER_URL}." }
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

    object RettTilInnsynRefVedlegg : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                    nynorsk { + "Du har rett til innsyn" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram."},
                    nynorsk { + "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «Dine rettar og plikter» for informasjon om korleis du går fram." }
                )
            }
        }
    }

    object RettTilInnsyn : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til innsyn" },
                    nynorsk { + "Du har rett til innsyn" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å se dokumentene i saken din. Du kan logge deg inn via nav.no for å se dokumenter i saken din."},
                    nynorsk { + "Du har rett til å sjå dokumenta i saka di. Du kan logge deg inn via nav.no for å sjå dokument i saka di. "}
                )
            }
        }
    }

    object HarDuSporsmal : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Har du spørsmål?" },
                    nynorsk { + "Har du spørsmål?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du finner mer informasjon på ${Constants.UFORE_URL}. " +
                            "På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. " +
                            "Hvis du ikke finner svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} " +
                            "hverdager kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                    },
                    nynorsk { + "Du finn meir informasjon på ${Constants.UFORE_URL}. " +
                            "På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. " +
                            "Dersom du ikkje finn svar på ${Constants.NAV_URL}, kan du ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE} " +
                            "kvardagar kl. ${Constants.NAV_KONTAKTSENTER_AAPNINGSTID}."
                    }
                )
            }
        }
    }
}