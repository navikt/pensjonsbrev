package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_OFFENTLIG_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KLAGE_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.KONTAKT_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_AAPNINGSTID
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON
import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/**
 * Felles avslutning for AFP etteroppgjør-vedtaksbrev (offentlig sektor):
 *   * Dine plikter (meldeplikt, tilbakekreving)
 *   * Du har rett til å klage
 *   * Du har rett til innsyn
 *   * Har du spørsmål? (med lenke til etteroppgjør-info)
 *
 * Brevene som har identisk klage-seksjon (PE_AF_04_102, PE_AF_04_106) inkluderer
 * hele [AfpEtteroppgjoerAvslutning]. Brev hvor klage-seksjonen har annen ordlyd
 * (f.eks. PE_AF_04_100 som har en ekstra «Du kan som nevnt sende inn
 * dokumentasjon»-paragraf) kan i stedet komponere fra sub-frasene
 * [DinePlikter], [DuHarRettTilInnsyn] og [HarDuSporsmal], og inline sin egen
 * klage-seksjon med TODO-merknad for faglig gjennomgang.
 */
object AfpEtteroppgjoerAvslutning : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        includePhrase(DinePlikter)
        includePhrase(DuHarRettTilAaKlageSeksUker)
        includePhrase(DuHarRettTilInnsyn)
        includePhrase(HarDuSporsmal)
    }

    /** Tittel «Dine plikter» + meldeplikt- og tilbakekrevingsparagrafene. */
    object DinePlikter : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Dine plikter" },
                    nynorsk { +"Dine plikter" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du har plikt til å melde fra om endringer som har betydning for størrelsen på " +
                            "pensjonen din."
                    },
                    nynorsk {
                        +"Du pliktar å melde frå om endringar som har noko å seie for storleiken på pensjonen " +
                            "din."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du må melde fra om endringer i egen inntekt, i familieforhold og ved flytting innen " +
                            "Norge eller til land utenfor Norge. Hvis du mottar grunnpensjon etter sats for " +
                            "enslige, må du melde fra hvis ektefellen, partneren eller samboeren din får en " +
                            "årlig inntekt som er større enn to ganger folketrygdens grunnbeløp eller hvis " +
                            "vedkommende får uføretrygd eller egen pensjon."
                    },
                    nynorsk {
                        +"Du må melde frå om endringar i eiga inntekt eller i familieforhold og ved flytting " +
                            "innanfor Noreg eller til land utanfor Noreg. Dersom du får grunnpensjon etter " +
                            "sats for einslege, må du melde frå dersom ektefellen, partnaren eller sambuaren " +
                            "din får ei årleg inntekt som er større enn to gonger grunnbeløpet i folketrygda, " +
                            "eller dersom vedkommande får eigen uføretrygd eller pensjon."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du får utbetalt for mye pensjon fordi du ikke har meldt fra om endringer, kan " +
                            "vi kreve tilbake det som er for mye utbetalt."
                    },
                    nynorsk {
                        +"Dersom du får utbetalt for mykje pensjon fordi du ikkje har meldt frå om endringar, " +
                            "kan vi krevje tilbake det som er for mykje utbetalt."
                    },
                )
            }
        }
    }

    /**
     * Tittel «Du har rett til å klage» + standard seks-ukers-klagefrist-paragraf.
     * Brukes av brev hvor klage-seksjonen kun består av denne ene paragrafen
     * (PE_AF_04_102, PE_AF_04_106). PE_AF_04_100 har en annen klage-seksjon med
     * en ekstra paragraf og inliner derfor sitt eget innhold.
     */
    object DuHarRettTilAaKlageSeksUker : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du " +
                            "mottok vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på " +
                            "$KLAGE_URL."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen " +
                            "då du fekk vedtaket. Klaga skal vere skriftleg. Du finn skjema og informasjon på " +
                            "$KLAGE_URL."
                    },
                )
            }
        }
    }

    /** Tittel «Du har rett til innsyn» + paragraf. */
    object DuHarRettTilInnsyn : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Du har rett til innsyn" },
                    nynorsk { +"Du har rett til innsyn" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har rett til å se dokumentene i saken din." },
                    nynorsk { +"Du har rett til å sjå dokumenta i saka di." },
                )
            }
        }
    }

    /** Tittel «Har du spørsmål?» + kontaktinformasjonsparagraf. */
    object HarDuSporsmal : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Har du spørsmål?" },
                    nynorsk { +"Har du spørsmål?" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du finner mer informasjon på $AFP_OFFENTLIG_URL#etteroppgjor. På $KONTAKT_URL " +
                            "kan du chatte eller skrive til oss. Hvis du ikke finner svar på $NAV_URL, kan du " +
                            "ringe oss på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, hverdager " +
                            "$NAV_KONTAKTSENTER_AAPNINGSTID."
                    },
                    nynorsk {
                        +"Du finn meir informasjon på $AFP_ETTEROPPGJOER_URL. På $KONTAKT_URL kan du " +
                            "chatte eller skrive til oss. Om du ikkje finn svar på $NAV_URL, kan du ringe oss " +
                            "på telefon $NAV_KONTAKTSENTER_TELEFON_PENSJON, kvardagar " +
                            "$NAV_KONTAKTSENTER_AAPNINGSTID."
                    },
                )
            }
        }
    }
}
