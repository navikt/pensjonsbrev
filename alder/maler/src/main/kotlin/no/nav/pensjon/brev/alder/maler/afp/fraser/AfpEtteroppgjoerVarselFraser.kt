package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

/**
 * Varsel-spesifikke fraser for fase-1-brevet «Foreløpig beregning av
 * AFP-etteroppgjør» (offentlig sektor / Statens pensjonskasse), konvertert fra
 * Exstream-malen `PE_AF_03_100`. Det aller meste av innholdet deles med
 * PE_AF_04-familien gjennom [AfpEtteroppgjoerInnhold], [AfpTilbakekrevingBody]
 * og [AfpEtteroppgjoerAvslutning]; her samler vi paragrafene som er unike for
 * varselet (foreløpig beregning, frist, og at det endelige vedtaket kommer
 * senere).
 */
object AfpEtteroppgjoerVarselFraser {

    /**
     * Den foreløpige beregningen som viser at bruker kan ha fått for mye
     * utbetalt. Tidsrommet varierer med [periode]-flaggene — samme firedeling
     * som fordelingsfrasen
     * [AfpEtteroppgjoerInnhold.InntektFoerUttakInntektEtterOpphoerFordelingPerPeriode].
     */
    data class ForeloepigBeregningForMye(
        val erHelAfpHeleAaret: Expression<Boolean>,
        val erUttakIAaret: Expression<Boolean>,
        val erOpphoerIAaret: Expression<Boolean>,
        val erUttakOgOpphoerIAaret: Expression<Boolean>,
        val uttaksdato: Expression<LocalDate>,
        val opphorsdato: Expression<LocalDate?>,
        val oppgjoersAar: Expression<Year>,
        val formyebetalt: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(erHelAfpHeleAaret) {
                paragraph {
                    text(
                        bokmal {
                            +"Den foreløpige beregningen av pensjonen for " + oppgjoersAar.format() +
                                    " viser at du kan ha fått " + formyebetalt.format() +
                                    " for mye utbetalt i pensjon."
                        },
                        nynorsk {
                            +"Den førebelse berekninga av pensjonen for " + oppgjoersAar.format() +
                                    " viser at du kan ha fått " + formyebetalt.format() +
                                    " for mykje utbetalt i pensjon."
                        },
                    )
                }
            }
            showIf(erUttakIAaret) {
                paragraph {
                    text(
                        bokmal {
                            +"Den foreløpige beregningen av pensjonen for tidsrommet " + uttaksdato.format() +
                                    " til 31. desember " + oppgjoersAar.format() + " viser at du kan ha fått " +
                                    formyebetalt.format() + " for mye utbetalt i pensjon."
                        },
                        nynorsk {
                            +"Den førebelse berekninga av pensjonen for tidsrommet " + uttaksdato.format() +
                                    " til 31. desember " + oppgjoersAar.format() + " viser at du kan ha fått " +
                                    formyebetalt.format() + " for mykje utbetalt i pensjon."
                        },
                    )
                }
            }
            showIf(erOpphoerIAaret) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Den foreløpige beregningen av pensjonen for tidsrommet 1. januar " +
                                        oppgjoersAar.format() + " til " + opphor.format() + " viser at du kan ha fått " +
                                        formyebetalt.format() + " for mye utbetalt i pensjon."
                            },
                            nynorsk {
                                +"Den førebelse berekninga av pensjonen for tidsrommet 1. januar " +
                                        oppgjoersAar.format() + " til " + opphor.format() + " viser at du kan ha fått " +
                                        formyebetalt.format() + " for mykje utbetalt i pensjon."
                            },
                        )
                    }
                }
            }
            showIf(erUttakOgOpphoerIAaret) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Den foreløpige beregningen av pensjonen for tidsrommet " + uttaksdato.format() +
                                        " til " + opphor.format() + " viser at du kan ha fått " + formyebetalt.format() +
                                        " for mye utbetalt i pensjon."
                            },
                            nynorsk {
                                +"Den førebelse berekninga av pensjonen for tidsrommet " + uttaksdato.format() +
                                        " til " + opphor.format() + " viser at du kan ha fått " + formyebetalt.format() +
                                        " for mykje utbetalt i pensjon."
                            },
                        )
                    }
                }
            }
        }
    }

    /** Hjemmelshenvisning i varsel-form: «Du finner reglene for etteroppgjør i loven om AFP …». */
    object HjemmelForeloepig : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du finner reglene for etteroppgjør i loven om AFP for medlemmer av Statens " +
                                "pensjonskasse § 3 bokstav d, og tilhørende forskrift om kombinasjon av " +
                                "avtalefestet pensjon for medlemmer av Statens pensjonskasse og arbeidsinntekt " +
                                "(pensjonsgivende inntekt)."
                    },
                    nynorsk {
                        +"Du finn reglane for etteroppgjer i loven om AFP for medlemmer av Statens " +
                                "pensjonskasse § 3 bokstav d, og tilhøyrande forskrift om kombinasjon av " +
                                "avtalefesta pensjon for medlemmer av Statens pensjonskasse og arbeidsinntekt " +
                                "(pensjonsgivande inntekt)."
                    },
                )
            }
        }
    }

    /** Paragraf om at det endelige vedtaket kommer etter at fristen for nye opplysninger er ute. */
    object VedtakOmEndeligResultatSenere : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket om det endelige resultatet av etteroppgjøret får du etter at fristen for " +
                                "å legge fram nye inntektsopplysninger er gått ut."
                    },
                    nynorsk {
                        +"Vedtaket om det endelege resultatet av etteroppgjeret får du etter at fristen for " +
                                "å leggje fram nye inntektsopplysningar er gått ut."
                    },
                )
            }
        }
    }

    /**
     * Toleransebeløp-paragraf i varsel-form: forventet arbeidsinntekt mot ny
     * beregnet inntekt, og at avviket overstiger toleransebeløpet slik at
     * pensjonen beregnes på nytt.
     */
    data class ToleransebeloepForeloepig(
        val forventetInntekt: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Ved beregning av pensjonen din la vi til grunn at du ville ha en forventet " +
                                "arbeidsinntekt på " + forventetInntekt.format() + ". Etter våre nye beregninger har " +
                                "du hatt en arbeidsinntekt i den perioden du har mottatt AFP som er høyere enn den " +
                                "arbeidsinntekten som ble lagt til grunn ved utbetalingen av pensjon for " +
                                oppgjoersAar.format() + ". Denne forskjellen er større enn toleransebeløpet som i " +
                                "2024 var på 15 000 kroner. Pensjonen din er derfor beregnet på nytt og avregnet " +
                                "mot den pensjonen du allerede har fått utbetalt i perioden."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din la vi til grunn at du ville ha ei forventa " +
                                "arbeidsinntekt på " + forventetInntekt.format() + ". Etter dei nye berekningane våre " +
                                "har du hatt ei arbeidsinntekt i den perioden du har fått AFP, som er høgare enn " +
                                "den arbeidsinntekta som blei lagd til grunn ved utbetalinga av pensjon for " +
                                oppgjoersAar.format() + ". Denne forskjellen er større enn toleransebeløpet som i " +
                                "2024 var på 15 000 kroner. Pensjonen din er derfor berekna på nytt og avrekna mot " +
                                "den pensjonen du allereie har fått utbetalt i perioden."
                    },
                )
            }
        }
    }

    /**
     * Avsluttende forklaring om at Nav fatter et endelig vedtak når fristen er
     * ute, og at for mye utbetalt AFP da må betales tilbake.
     */
    data class NaarFristenHarGaattUt(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Når fristen for å sende inn dokumentasjon har gått ut, fatter Nav et endelig " +
                                "vedtak om hvor høy AFP du hadde rett til i " + oppgjoersAar.format() + ". Hvis " +
                                "beregningen fortsatt viser at du har fått utbetalt for mye AFP, må det som er " +
                                "utbetalt for mye betales tilbake. Du får nærmere informasjon om hvordan " +
                                "tilbakebetaling kan gjøres i det samme vedtaket. Innbetalt skatt vil bli trukket " +
                                "fra beløpet vi krever tilbake."
                    },
                    nynorsk {
                        +"Når fristen for å sende inn dokumentasjon har gått ut, gjer Nav eit endeleg " +
                                "vedtak om kor høg AFP du hadde rett til i " + oppgjoersAar.format() + ". Dersom " +
                                "berekninga framleis viser at du har fått utbetalt for mykje AFP, må det som er " +
                                "utbetalt for mykje betalast tilbake. Du får nærmare informasjon om korleis " +
                                "tilbakebetaling kan gjerast i det same vedtaket. Innbetalt skatt vil bli trekt " +
                                "frå beløpet vi krev tilbake."
                    },
                )
            }
        }
    }

    /** Forbehold om at ektefelletillegg ikke er hensyntatt og kan endres i eget vedtak. */
    object EktefelletilleggForbehold : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"I vår pensjonsberegning er det ikke tatt hensyn til eventuelt ektefelletillegg til " +
                                "forsørget ektefelle over 60 år. Dersom du har fått utbetalt ektefelletillegg, kan " +
                                "nye inntektsopplysninger føre til endring av tillegget. Du vil få eget vedtak om " +
                                "dette."
                    },
                    nynorsk {
                        +"I pensjonsberekninga vår er det ikkje teke omsyn til eventuelt ektefelletillegg til " +
                                "forsørgd ektefelle over 60 år. Dersom du har fått utbetalt ektefelletillegg, kan " +
                                "nye inntektsopplysningar føre til at tillegget blir endra. Du vil få eige vedtak " +
                                "om dette."
                    },
                )
            }
        }
    }

    /** Tittel «Dine rettigheter og plikter» + retten til innsyn etter forvaltningsloven § 18. */
    object DineRettigheterOgPlikterInnsyn : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Dine rettigheter og plikter" },
                    nynorsk { +"Dine rettar og plikter" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Vi gjør oppmerksom på at du etter forvaltningsloven paragraf 18 har rett til å se " +
                                "sakens dokumenter."
                    },
                    nynorsk {
                        +"Vi gjer merksam på at du etter forvaltingslova paragraf 18 har rett til å sjå " +
                                "saksdokumenta."
                    },
                )
            }
        }
    }

    /** Oppfordring om å ta kontakt med Nav for mer informasjon (som i Exstream-kilden 03_100/03_101). */
    object TaKontaktForMerInformasjon : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Ta gjerne kontakt med Nav hvis du ønsker mer informasjon. Husk at du kan finne mer " +
                                "informasjon om AFP på $NAV_URL."
                    },
                    nynorsk {
                        +"Ta gjerne kontakt med Nav dersom du ønskjer meir informasjon. Hugs at du kan finne meir " +
                                "informasjon om AFP på $NAV_URL."
                    },
                )
            }
        }
    }

    /** Avsluttende meldeplikt-paragraf om endringer i arbeidsinntekt. */
    object MeldepliktArbeidsinntekt : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Det er viktig at du melder fra til Nav om endringer i arbeidsinntekt som kan ha " +
                                "betydning for størrelsen på den månedlige pensjonen din."
                    },
                    nynorsk {
                        +"Det er viktig at du melder frå til Nav om endringar i arbeidsinntekt som kan ha " +
                                "noko å seie for storleiken på den månadlege pensjonen din."
                    },
                )
            }
        }
    }
}
