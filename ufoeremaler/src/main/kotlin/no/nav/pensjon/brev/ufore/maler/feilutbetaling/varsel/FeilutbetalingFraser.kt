package no.nav.pensjon.brev.ufore.maler.feilutbetaling.varsel

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.maler.fraser.Constants

class FeilutbetalingFraser {

    object SlikUttalerDuDeg : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Slik uttaler du deg " },
                    nynorsk { + "Slik uttaler du deg " })
            }
            paragraph {
                text(
                    bokmal { + "Hvis du kan dokumentere at vi har gitt deg feil eller mangelfull informasjon, eller du har hatt en alvorlig helsetilstand, ber vi deg om å uttale deg og gi oss denne informasjonen. " },
                    nynorsk { + "Dersom du kan dokumentere at vi har gitt deg feil eller mangelfull informasjon, eller du har hatt ein alvorleg helsetilstand, ber vi deg om å uttale deg og gi oss denne informasjonen. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å uttale deg før vi tar den endelige avgjørelsen om tilbakebetaling og fatter et vedtak. Du kan skrive til oss på ${Constants.KONTAKT_URL} eller ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE}. " },
                    nynorsk { + "Du har rett til å uttale deg før vi tek den endelege avgjerda om tilbakebetaling og fattar eit vedtak. Du kan skrive til oss på ${Constants.KONTAKT_URL} eller ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_UFORE}. "}
                )
            }
        }
    }

    object KriterierIngenTilbakekreving : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Hvis vi ikke kan kreve tilbake alt som er utbetalt for mye må " },
                    nynorsk { + "Dersom vi ikkje kan krevje tilbake alt som er utbetalt for mykje, må "}
                )
            }
            paragraph {
                list {
                    item {
                        text(
                        bokmal { + "informasjonen vi har gitt til deg være feil eller svært mangelfull " },
                        nynorsk { + "informasjonen vi har gitt til deg vere feil eller svært mangelfull "}
                        )
                    }
                    item {
                        text(
                        bokmal { + "du grunnet en alvorlig helsetilstand, ikke har vært i stand til å forstå at utbetalingen var feil. " },
                        nynorsk { + "du, grunna ein alvorleg helsetilstand, ikkje ha vore i stand til å forstå at utbetalinga var feil. "}
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { + "Dette står i folketrygdloven § 22-15 første ledd. " },
                    nynorsk { + "Dette står i folketrygdlova § 22-15 første ledd. "}
                )
            }
        }
    }

    object KriterierTilbakekreving : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "I disse tilfellene kan vi kreve tilbake feilutbetalt beløp "},
                    nynorsk { + "I desse tilfella kan vi krevje tilbake feilutbetalt beløp "},
                )
            }
            paragraph {
                text(
                    bokmal { + "Når vi har utbetalt for mye uføretrygd, kan vi i nesten alle tilfeller kreve tilbake alt, eller deler av beløpet som er utbetalt. " +
                        "Det gjelder uansett om det er du eller vi som har gjort feilen som har ført til feilutbetalingen. " },
                    nynorsk { + "Når vi har utbetalt for mykje uføretrygd, kan vi i nesten alle tilfelle krevje tilbake alt, eller delar av beløpet som er utbetalt. " +
                            "Det gjeld uansett om det er du eller vi som har gjort feilen som har ført til feilutbetalinga. " }
                )
            }
            paragraph {
                text(
                    bokmal { + "I hver enkelt sak vil vi vurdere kvaliteten på informasjonen vi har gitt deg. Vi må også " +
                        "vurdere om du har vært i stand til å forstå informasjonen, eller burde ha forstått at det var en feilutbetaling. " },
                    nynorsk { + "I kvar enkelt sak vil vi vurdere kvaliteten på informasjonen vi har gitt deg. Vi må òg " +
                            "vurdere om du har vore i stand til å forstå informasjonen, eller burde ha forstått at det var ei feilutbetaling. "}
                )
            }
        }
    }

    object HvaSkjerVidere : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Hva skjer videre i saken din " },
                    nynorsk { + "Kva skjer vidare i saka di "}
                    )
            }
            paragraph {
                text(
                    bokmal { + "Vi vil vurdere saken og sende deg et vedtak. Hvis du må betale tilbake til oss, vil det stå informasjon i vedtaksbrevet om hvilke muligheter du har for å betale tilbake. " },
                    nynorsk { + "Vi vil vurdere saka og sende deg eit vedtak. Dersom du må betale tilbake til oss, vil det stå informasjon i vedtaksbrevet om kva moglegheiter du har for å betale tilbake. " }
                    )
            }
        }
    }

    object FeilOpplysninger : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { + "Dette kan du gjøre hvis vi har feil opplysninger " },
                    nynorsk { + "Dette kan du gjere dersom vi har feil opplysningar " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har rett til å uttale deg og gi oss nye opplysninger før vi fatter et vedtak. " +
                            "Det må du gjøre innen 14 dager etter at du har fått dette varselet, se avsnittet «Slik uttaler du deg» for mer informasjon. " },
                    nynorsk { + "Du har rett til å uttale deg og gi oss nye opplysningar før vi fattar eit vedtak. " +
                            "Det må du gjere innan 14 dagar etter at du har fått dette varselet. Sjå avsnittet «Slik uttaler du deg» for meir informasjon. " },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dette er bare et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. " +
                            "Du får et vedtak når saken er ferdig behandlet. " } ,
                    nynorsk { + "Dette er berre eit varsel om at vi vurderer å krevje tilbake det feilutbetalte beløpet. " +
                            "Du får eit vedtak når saka er ferdig behandla. " },
                )
            }
        }
    }

}