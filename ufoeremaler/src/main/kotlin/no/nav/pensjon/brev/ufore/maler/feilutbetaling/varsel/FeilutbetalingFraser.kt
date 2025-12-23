package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

class FeilutbetalingFraser {

    object SlikUttalerDuDeg : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(bokmal { + "Slik uttaler du deg " })
            }
            paragraph {
                text(bokmal { + "Hvis du kan dokumentere at vi har gitt deg feil eller mangelfull informasjon, eller du har hatt en alvorlig helsetilstand, ber vi deg om å uttale deg og gi oss denne informasjonen. " })
            }
            paragraph {
                text(bokmal { + "Du har rett til å uttale deg før vi tar den endelige avgjørelsen om tilbakebetaling og fatter et vedtak. Du kan skrive til oss på nav.no/kontakt eller ringe oss på telefon 55 55 33 33. " })
            }
        }
    }

    object KriterierIngenTilbakekreving : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(bokmal { + "Hvis vi ikke kan kreve tilbake alt som er utbetalt for mye må " })
            }
            paragraph {
                list {
                    item { bokmal { + "informasjonen vi har gitt til deg være feil eller svært mangelfull " } }
                    item { bokmal { + "du grunnet en alvorlig helsetilstand, ikke har vært i stand til å forstå at utbetalingen var feil. " } }
                }
            }
            paragraph {
                text(bokmal { + "Dette står i folketrygdloven § 22-15 første ledd, første punktum. " })
            }
        }
    }

    object KriterierTilbakekreving : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(bokmal { + "I disse tilfellene kan vi kreve tilbake feilutbetalt beløp "})
            }
            paragraph {
                text(bokmal { + "Når vi har utbetalt for mye uføretrygd, kan vi i nesten alle tilfeller kreve tilbake alt, eller deler av beløpet som er utbetalt. " +
                        "Det gjelder uansett om det er du eller vi som har gjort feilen som har ført til feilutbetalingen. " })
            }
            paragraph {
                text(bokmal { + "I hver enkelt sak vil vi vurdere kvaliteten på informasjonen vi har gitt deg. Vi må også " +
                        "vurdere om du har vært i stand til å forstå informasjonen, eller burde ha forstått at det var en feilutbetaling. " })
            }
        }
    }

    object HvaSkjerVidere : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(bokmal { + "Hva skjer videre i saken din " })
            }
            paragraph {
                text(bokmal { + "Vi vil vurdere saken og sende deg et vedtak. Hvis du må betale tilbake til oss, vil det stå informasjon i vedtaksbrevet om hvilke muligheter du har for å betale tilbake. " })
            }
        }
    }

    object FeilOpplysninger : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Dette kan du gjøre hvis vi har feil opplysninger " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å uttale deg og gi oss nye opplysninger før vi fatter et vedtak. " +
                            "Det må du gjøre innen 14 dager etter at du har fått dette varselet, se avsnittet \"Slik uttaler du deg\" for mer informasjon. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Dette er bare et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. " +
                            "Du får et vedtak når saken er ferdig behandlet. " }
                )
            }
        }
    }

}