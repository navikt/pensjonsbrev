package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.maler.fraser.Constants.KLAGE_URL
import no.nav.pensjon.brev.maler.fraser.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.maler.fraser.Constants.NAV_KONTAKTSENTER_TELEFON_UFORE
import no.nav.pensjon.brev.maler.fraser.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.Constants.UFORE_URL
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class Felles {

    object RettTilAKlage : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Dine rettigheter og plikter»"
                        + " får du vite mer om hvordan du går fram. Du finner skjema og informasjon på $KLAGE_URL."},
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
                    bokmal { + "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram."}
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
                    bokmal { + "Du finner mer informasjon på $UFORE_URL. " +
                            "På $KONTAKT_URL kan du chatte eller skrive til oss. " +
                            "Hvis du ikke finner svar på $NAV_URL, kan du ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_UFORE " +
                            "hverdager kl. $NAV_KONTAKTSENTER_AAPNINGSTID."
                    }
                )
            }
        }
    }
}