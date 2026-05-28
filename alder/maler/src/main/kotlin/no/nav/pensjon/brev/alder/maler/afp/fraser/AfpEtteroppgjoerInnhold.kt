package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
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
 * Felles innhold som går igjen i AFP etteroppgjør-vedtaksbrev (offentlig sektor /
 * Statens pensjonskasse). Avslutningen (Dine plikter / klage / innsyn / spørsmål)
 * ligger i [AfpEtteroppgjoerAvslutning]; her samler vi de øvrige paragrafene som
 * deles mellom brevene `VedtakAfpEtteroppgjoerIngenEndringAuto` (PE_AF_04_102),
 * `VedtakAfpEtteroppgjoerToleransebeloepAuto` (PE_AF_04_100) m.fl.
 */
object AfpEtteroppgjoerInnhold {

    /**
     * Innledning som forklarer at NAV gjør et årlig etteroppgjør på grunnlag av
     * inntektsopplysninger fra Skatteetaten. Identisk i alle ingen-endring-brev.
     */
    object EtteroppgjoerIntro : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Hvert år foretar NAV en kontroll av AFP når resultatet av skatteoppgjøret " +
                            "foreligger. Hvis du har hatt en annen pensjonsgivende inntekt fra arbeid eller " +
                            "næring enn den forventede inntekten som ble lagt til grunn ved utbetalingen, skal " +
                            "vi foreta en ny beregning av pensjonen på grunnlag av inntektsopplysningene fra " +
                            "Skatteetaten."
                    },
                    nynorsk {
                        +"Kvart år gjennomfører NAV ein kontroll av AFP når resultatet av skatteoppgjeret er " +
                            "klart. Dersom du har hatt ei anna pensjonsgivande inntekt frå arbeid eller næring " +
                            "enn den framtidige inntekta som blei lagd til grunn ved utbetalinga, skal vi " +
                            "berekne pensjonen på nytt på grunnlag av inntektsopplysningane frå Skatteetaten."
                    },
                )
            }
        }
    }

    /**
     * Konklusjonsparagraf: vi har ikke funnet grunnlag for å endre beregningen
     * av pensjonen for {oppgjørsår}.
     */
    data class IkkeFunnetGrunnlagForAaEndre(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Vi har ikke funnet grunnlag for å endre beregningen av pensjonen din i " +
                            oppgjoersAar.format() + " på bakgrunn av inntektsopplysningene fra Skatteetaten."
                    },
                    nynorsk {
                        +"Vi har ikkje funne grunnlag for å endre berekninga av pensjonen din i " +
                            oppgjoersAar.format() + " på bakgrunn av inntektsopplysningane frå Skatteetaten."
                    },
                )
            }
        }
    }

    /**
     * Hjemmelshenvisning til AFP-loven for medlemmer av Statens pensjonskasse.
     */
    object VedtaksgrunnlagAfpSpk : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 " +
                            "bokstav d, og tilhørende forskrift om kombinasjon av avtalefestet pensjon for " +
                            "medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt)."
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 " +
                            "bokstav d og tilhøyrande forskrift om kombinasjon av avtalefestet pensjon for " +
                            "medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt)."
                    },
                )
            }
        }
    }

    object VedtaksgrunnlagAfpApotekerordningen : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av Apotekerne XXX."
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter lov om AFP for medlemmer av  Apotekerne XXX."
                    },
                )
            }
        }
    }

    /** Title1 «Inntekten din i {oppgjørsår}». */
    data class InntektenDinIAarTittel(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Inntekten din i " + oppgjoersAar.format() },
                    nynorsk { +"Inntekta di i " + oppgjoersAar.format() },
                )
            }
        }
    }

    /**
     * Paragrafen «Opplysninger fra Skatteetaten viser at du har hatt en
     * samlet pensjonsgivende inntekt på {pgi} i inntektsåret {oppgjørsår}.»
     * Identisk mellom PE_AF_04_101 og PE_AF_04_102.
     */
    data class SamletPgiOpplysning(
        val pensjonsgivendeInntekt: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Opplysninger fra Skatteetaten viser at du har hatt en samlet pensjonsgivende " +
                            "inntekt på " + pensjonsgivendeInntekt.format() + " i inntektsåret " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Opplysningar frå Skatteetaten viser at du har hatt ei samla pensjonsgivande " +
                            "inntekt på " + pensjonsgivendeInntekt.format() + " i inntektsåret " + oppgjoersAar.format() + "."
                    },
                )
            }
        }
    }

    /** Avsluttende konklusjonsparagraf: pensjonsberegningen blir derfor ikke endret. */
    data class PensjonsberegningenBlirIkkeEndret(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Pensjonsberegningen for " + oppgjoersAar.format() + " blir derfor ikke endret." },
                    nynorsk { +"Pensjonsberekninga for " + oppgjoersAar.format() + " blir derfor ikkje endra." },
                )
            }
        }
    }

    /**
     * Innledning til etteroppgjør-fase-2-brev der utbetalingen har vært riktig:
     * «Vi viser til tidligere brev … har vært riktig … er derfor avsluttet.»
     * Delt mellom PE_AF_04_103 og PE_AF_04_106.
     */
    data class HarVaertRiktigIntro(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til tidligere brev om etteroppgjør av avtalefestet pensjon (AFP) for " +
                            oppgjoersAar.format() + ". Resultatet av etteroppgjøret for " +
                            oppgjoersAar.format() + " viser at utbetalingen av AFP i " +
                            oppgjoersAar.format() + " har vært riktig. Etteroppgjøret for " +
                            oppgjoersAar.format() + " er derfor avsluttet."
                    },
                    nynorsk {
                        +"Vi viser til tidlegare brev om etteroppgjer av avtalefesta pensjon (AFP) for " +
                            oppgjoersAar.format() + ". Resultatet av etteroppgjeret for " +
                            oppgjoersAar.format() + " viser at utbetalinga av AFP i " +
                            oppgjoersAar.format() + " har vore rett. Etteroppgjeret for " +
                            oppgjoersAar.format() + " er derfor avslutta."
                    },
                )
            }
        }
    }

    /**
     * Avsluttende konklusjonsparagraf: ny beregning fører ikke til tilbakekreving.
     * Delt mellom PE_AF_04_103 og PE_AF_04_106.
     */
    data class NyBeregningFoererIkkeTilTilbakekreving(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Ny beregning av etteroppgjøret for " + oppgjoersAar.format() +
                            " fører til at det ikke blir tilbakekreving."
                    },
                    nynorsk {
                        +"Ny berekning av etteroppgjeret for " + oppgjoersAar.format() +
                            " fører til at det ikkje blir tilbakekrevjing."
                    },
                )
            }
        }
    }

    /**
     * Tittel «Melding om endringer av inntekten» + de to innledende paragrafene
     * som er identiske mellom PE_AF_04_100 og PE_AF_04_102.
     */
    object MeldingOmEndringerInnledning : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Melding om endringer av inntekten" },
                    nynorsk { +"Melding om endringar av inntekta" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at inntekten som ligger til grunn for beregningen er feil, må du " +
                            "melde fra til oss innen "
                    },
                    nynorsk {
                        +"Dersom du meiner at inntekta som ligg til grunn for berekninga, er feil, må du " +
                            "melde frå til oss innan "
                    },
                )
                text(
                    bokmal { +"fire uker" },
                    nynorsk { +"fire veker" },
                    BOLD,
                )
                text(
                    bokmal { +" fra du mottok dette brevet." },
                    nynorsk { +" frå du fekk dette brevet." },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Inntekt fra arbeid eller virksomhet som er opptjent før første uttak av AFP " +
                            "skal holdes utenfor etteroppgjøret. Dette gjelder også hvis inntekten er " +
                            "utbetalt etter at du tok ut AFP. Med første uttak menes første gang du tok " +
                            "ut AFP, uavhengig av om du har tatt ut gradert eller hel pensjon."
                    },
                    nynorsk {
                        +"Inntekt frå arbeid eller verksemd som er opptent før det første uttaket av AFP, " +
                            "skal haldast utanfor etteroppgjeret. Det gjeld også dersom inntekta er " +
                            "utbetalt etter at du tok ut AFP. Med første uttak meiner vi første gong du " +
                            "tok ut AFP, uavhengig av om du har teke ut gradert eller heil pensjon."
                    },
                )
            }
        }
    }

    /**
     * Paragrafen «Annen inntekt som er opptjent samtidig med utbetaling av AFP...».
     * Identisk mellom PE_AF_04_100 og PE_AF_04_102.
     */
    object AnnenInntektInntektsproevd : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Annen inntekt som er opptjent samtidig med utbetaling av AFP, vil bli " +
                            "inntektsprøvd mot pensjonen. Vi gjør for ordens skyld oppmerksom på at " +
                            "feriepenger opptjent etter første uttak av AFP skal inntektsprøves mot " +
                            "pensjonen."
                    },
                    nynorsk {
                        +"Anna inntekt som er opptent samtidig med utbetaling av AFP, vil bli " +
                            "inntektsprøvd mot pensjonen. Vi gjer for ordens skuld merksam på at " +
                            "feriepengar som er opptente etter uttak av AFP, skal inntektsprøvast mot " +
                            "pensjonen."
                    },
                )
            }
        }
    }

    object DokumenterInntekterUtenforAvkorting : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Du må dokumentere hvilke av inntektene dine som skal holdes utenfor avkorting " +
                            "av AFP. Hvis du ikke sender inn ny dokumentasjon innen fristen, benytter vi " +
                            "de opplysningene vi har, og etteroppgjøret vil bli vurdert som avsluttet."
                    },
                    nynorsk {
                        +"Du må dokumentere kva delar av inntektene dine som skal haldast utanfor " +
                            "avkorting av AFP. Dersom du ikkje sender inn ny dokumentasjon innan fristen, " +
                            "nyttar vi dei opplysningane vi har, og etteroppgjeret blir rekna som avslutta."
                    },
                )
            }
        }
    }

    /**
     * Paragrafen «På {url} finner du et skjema...». Identisk mellom
     * PE_AF_04_100, PE_AF_04_101 og PE_AF_04_102.
     */
    object SkjemaForDokumentasjon : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"På $AFP_ETTEROPPGJOER_URL finner du et skjema som du eller arbeidsgiver kan " +
                            "benytte ved innsending av dokumentasjon."
                    },
                    nynorsk {
                        +"På $AFP_ETTEROPPGJOER_URL finn du eit skjema som du eller arbeidsgivaren kan " +
                            "nytte ved innsending av dokumentasjon."
                    },
                )
            }
        }
    }

    /**
     * Title1 «Spesielt om inntekter opptjent i forbindelse med covid-19» + de to
     * påfølgende paragrafene som er identiske mellom PE_AF_04_100 og PE_AF_04_102.
     */
    object SpesieltOmCovidInntekterInnledning : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Spesielt om inntekter opptjent i forbindelse med covid-19" },
                    nynorsk { +"Spesielt om inntekter opptente i samband med covid-19" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Dersom du har påtatt deg ekstra covid-19-relatert arbeid ut over det du " +
                            "vanligvis jobber, kan det hende at inntekten din fra dette arbeidet ikke " +
                            "skal gi avkorting av AFP. Dette gjelder feriepenger av inntekt fra tjeneste " +
                            "fram til 30. juni 2023 som var nødvendig for å ivareta et ekstraordinært " +
                            "personellbehov ved TISK-tiltak (testing, isolering, smittesporing og " +
                            "karantene) eller vaksinering. Det gjelder bare tjeneste med pensjonistlønn " +
                            "etter særskilt sats."
                    },
                    nynorsk {
                        +"Dersom du har teke på deg ekstra covid-19-relatert arbeid utover det du " +
                            "vanlegvis jobbar, kan det hende at inntekta di frå dette arbeidet ikkje " +
                            "skal gi avkorting av AFP. Dette gjeld feriepengane av inntekt frå teneste " +
                            "fram til 30. juni 2023 som var naudsynt for å ivareta personellbehov ved " +
                            "TISK-tiltak (testing, isolering, smittesporing og karantene) eller " +
                            "vaksinering. Det gjeld berre teneste med pensjonistlønn etter særskilt sats."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Reglene om dette finnes i forskrift 19. september 2023 nr. 1446 om endring i " +
                            "forskrift om kombinasjon av avtalefestet pensjon for medlemmer av Statens " +
                            "pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt) kapittel II nr. 2."
                    },
                    nynorsk {
                        +"Reglane om dette finst i forskrift 19. september 2023 nr. 1446 om endring i " +
                            "forskrift om kombinasjon av avtalefestet pensjon for medlemmer av Statens " +
                            "pensjonskasse og arbeidsinntekt (pensjonsgivande inntekt) kapittel II nr. 2."
                    },
                )
            }
        }
    }

    /**
     * Title1 «Spesielt om unntak ... fordrevne fra Ukraina» + den påfølgende
     * forklaringsparagrafen. Identisk mellom PE_AF_04_100 og PE_AF_04_102.
     */
    object SpesieltOmUkrainaUnntak : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal {
                        +"Spesielt om unntak for pensjonistlønn etter særskilt sats for pensjonister som " +
                            "jobber med fordrevne fra Ukraina"
                    },
                    nynorsk {
                        +"Spesielt om unntak for pensjonistlønn etter særskilt sats for pensjonistar som " +
                            "har arbeidd med fordrivne frå Ukraina"
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Dersom du har arbeidet med fordrevne fra Ukraina og mottatt pensjonistlønn etter " +
                            "særskilt sats kan inntekten for dette arbeidet unntas fra inntektsavkorting. " +
                            "Det samme gjelder feriepenger for arbeid utført etter 1. april 2022."
                    },
                    nynorsk {
                        +"Dersom du har arbeidd med fordrivne frå Ukraina og motteke pensjonistlønn etter " +
                            "særskilt sats kan inntekta for dette arbeidet unntakas frå inntektsavkorting. " +
                            "Det same gjeld feriepengar for slikt arbeid utført etter 1. april 2022."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Reglene om inntekter fra arbeid med fordrevne fra Ukraina finnes i forskrift om " +
                            "kombinasjon av AFP og arbeidsinntekt § 2 tredje ledd."
                    },
                    nynorsk {
                        +"Reglane om inntekter frå arbeid med fordrivne frå Ukraina finst i forskrift " +
                            "om kombinasjon av AFP og arbeidsinntekt § 2 tredje ledd."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"For at Nav skal kunne holde slike inntekter utenfor avkorting, må du sende oss " +
                                "dokumentasjon. Se mer på $AFP_ETTEROPPGJOER_URL om hvordan du sender " +
                                "dokumentasjon. Vi trenger bekreftelse fra arbeidsgiveren din om følgende:"
                    },
                    nynorsk {
                        +"For at Nav skal kunne halde slike inntekter utanfor avkorting, må du sende oss " +
                                "dokumentasjon. Sjå meir på $AFP_ETTEROPPGJOER_URL om korleis du sender " +
                                "dokumentasjon. Vi treng stadfesting frå arbeidsgivaren din om følgjande:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal { +"hvor mye du har hatt i feriepenger fra slikt ekstra arbeid" },
                            nynorsk { +"kor mykje du har hatt i feriepengar frå slikt ekstra arbeid" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"i hvilken tidsperiode(-r) dette gjelder" },
                            nynorsk { +"i kva for tidsperiode (-periodar) dette gjeld" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"om utbetalingen er gjort etter særskilt sats for pensjonistavlønning" },
                            nynorsk { +"om utbetalinga er gjort etter særskilt sats for pensjonistavlønning" },
                        )
                    }
                }
            }

        }
    }

    /**
     * Fordeling av pensjonsgivende inntekt på periodene med og uten AFP.
     * Brukes i etterbetalingsbrev (PE_AF_04_101) og toleransebeløpsbrev
     * (PE_AF_04_107) som begge har en 4-veis Periode-diskriminator. Phrasen
     * tar én `Expression<Boolean>` per case slik at hvert brev kan koble sin
     * lokale `Periode`-enum til riktig gren uten å dele en felles type.
     *
     * Komma-bare nynorsk-forskjeller mellom de to brevene er bevart i én
     * versjon (med komma etter datoselektoren), og «AFP er avslutta» er valgt
     * over «AFP tok slutt» i OPPHOER_I_AARET-paragrafens IEO-tekst.
     */
    data class IfuIeoFordelingPerPeriode(
        val erHelAfpHeleAaret: Expression<Boolean>,
        val erUttakIAaret: Expression<Boolean>,
        val erOpphoerIAaret: Expression<Boolean>,
        val erUttakOgOpphoerIAaret: Expression<Boolean>,
        val uttaksdato: Expression<LocalDate>,
        val opphorsdato: Expression<LocalDate?>,
        val oppgjoersAar: Expression<Year>,
        val inntektFoerUttak: Expression<Kroner>,
        val inntektEtterOpphoer: Expression<Kroner>,
        val inntektIAfpPerioden: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(erHelAfpHeleAaret) {
                paragraph {
                    text(
                        bokmal { +"Vi har lagt til grunn at hele denne inntekten er opptjent samtidig som du har mottatt AFP." },
                        nynorsk { +"Vi har lagt til grunn at heile denne inntekta er opptent samtidig som du har fått AFP." },
                    )
                }
            }
            showIf(erUttakIAaret) {
                paragraph {
                    text(
                        bokmal {
                            +"Fordi du har tatt ut AFP fra " + uttaksdato.format() + " benytter vi en " +
                                "standardberegning for å beregne fordelingen av inntekten din før og etter " +
                                "AFP-uttaket. Denne beregningen kan endres dersom du kan dokumentere en " +
                                "annen fordeling av inntekten."
                        },
                        nynorsk {
                            +"Fordi du har teke ut AFP frå " + uttaksdato.format() + ", nyttar vi ei " +
                                "standardberekning for å rekne ut fordelinga av inntekta di før og etter " +
                                "AFP-uttaket. Denne berekninga kan endrast dersom du kan dokumentere ei " +
                                "anna fordeling av inntekta."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Vi har lagt til grunn at " + inntektFoerUttak.format() + " er opptjent før du tok ut AFP. " +
                                "Dette beløpet skal holdes utenfor etteroppgjøret for " +
                                oppgjoersAar.format() + ". Den delen av inntekten som regnes for å være " +
                                "opptjent i den perioden du har mottatt AFP, er beregnet til " +
                                inntektIAfpPerioden.format() + "."
                        },
                        nynorsk {
                            +"Vi har lagt til grunn at " + inntektFoerUttak.format() + " er opptente før du tok ut AFP. " +
                                "Dette beløpet skal haldast utanfor etteroppgjeret for " +
                                oppgjoersAar.format() + ". Den delen av inntekta som blir rekna for å vere " +
                                "opptent i den perioden du har fått AFP, er berekna til " +
                                inntektIAfpPerioden.format() + "."
                        },
                    )
                }
            }
            showIf(erOpphoerIAaret) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Fordi AFP har opphørt fra " + opphor.format() + " benytter vi en " +
                                    "standardberegning for å beregne fordelingen av inntekten din i " +
                                    "perioder med og uten AFP. Denne beregningen kan endres dersom du kan " +
                                    "dokumentere en annen fordeling av inntekten."
                            },
                            nynorsk {
                                +"Fordi AFP er avslutta frå " + opphor.format() + ", nyttar vi ei " +
                                    "standardberekning for å rekne ut fordelinga av inntekta di i periodar " +
                                    "med og utan AFP. Denne berekninga kan endrast dersom du kan " +
                                    "dokumentere ei anna fordeling av inntekta."
                            },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal {
                            +"Vi har lagt til grunn at " + inntektEtterOpphoer.format() + " er opptjent etter at du gikk " +
                                "over fra AFP til annen pensjon, eventuelt etter opphør av AFP. Dette " +
                                "beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() +
                                ". Den delen av inntekten som regnes for å være opptjent i den perioden du " +
                                "har mottatt AFP, er beregnet til " + inntektIAfpPerioden.format() + "."
                        },
                        nynorsk {
                            +"Vi har lagt til grunn at " + inntektEtterOpphoer.format() + " er opptente etter at du gjekk " +
                                "over frå AFP til annan pensjon, eventuelt etter at AFP er avslutta. Dette " +
                                "beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                                ". Den delen av inntekta som blir rekna for å vere opptent i den perioden " +
                                "du har fått AFP, er berekna til " + inntektIAfpPerioden.format() + "."
                        },
                    )
                }
            }
            showIf(erUttakOgOpphoerIAaret) {
                paragraph {
                    text(
                        bokmal {
                            +"Fordi du ikke har hatt rett til AFP hele året benytter vi en " +
                                "standardberegning for å beregne fordelingen av inntekten din i perioder " +
                                "med og uten AFP i det aktuelle året. Denne beregningen kan endres dersom " +
                                "du kan dokumentere en annen fordeling av inntekten."
                        },
                        nynorsk {
                            +"Fordi du ikkje har hatt rett til AFP heile året, nyttar vi ei " +
                                "standardberekning for å rekne ut fordelinga av inntekta di i periodar med " +
                                "og utan AFP i det aktuelle året. Denne berekninga kan endrast dersom du " +
                                "kan dokumentere ei anna fordeling av inntekta."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Vi har lagt til grunn at du tjente " + inntektFoerUttak.format() + " før du tok ut AFP og " +
                                inntektEtterOpphoer.format() + " etter at du gikk over fra AFP til annen pensjon, " +
                                "eventuelt etter opphør av AFP. Det samlede beløpet holdes utenfor " +
                                "etteroppgjøret. Den delen av inntekten som regnes for å være opptjent i " +
                                "den perioden du har mottatt AFP, er beregnet til " + inntektIAfpPerioden.format() + "."
                        },
                        nynorsk {
                            +"Vi har lagt til grunn at du tente " + inntektFoerUttak.format() + " før du tok ut AFP og " +
                                inntektEtterOpphoer.format() + " etter at du gjekk over frå AFP til annan pensjon, " +
                                "eventuelt etter at AFP tok slutt. Det samla beløpet blir halde utanfor " +
                                "etteroppgjeret. Den delen av inntekta som blir rekna for å vere opptent i " +
                                "den perioden du har fått AFP, er berekna til " + inntektIAfpPerioden.format() + "."
                        },
                    )
                }
            }
        }
    }

    /**
     * Hele «Ny pensjonsberegning»-blokken som avslutter etterbetalingsbrevene
     */
    data class NyPensjonsberegningEtterbetalingBlokk(
        val erHeleAaret: Expression<Boolean>,
        val erUttakIAaret: Expression<Boolean>,
        val erOpphoerIAaret: Expression<Boolean>,
        val erUttakOgOpphoerIAaret: Expression<Boolean>,
        val uttaksdato: Expression<LocalDate>,
        val opphorsdato: Expression<LocalDate?>,
        val oppgjoersAar: Expression<Year>,
        val fullAfp: Expression<Kroner>,
        val fradragBeregnetArbeidsInntekt: Expression<Kroner>,
        val inntektIAfpPerioden: Expression<Kroner>,
        val tidligereArbeidsInntektBeregnet: Expression<Kroner>,
        val korrigertAfp: Expression<Kroner>,
        val utbetaltAfp: Expression<Kroner>,
        val forlitebetalt: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Ny pensjonsberegning" },
                    nynorsk { +"Ny pensjonsberekning" },
                )
            }

            showIf(erHeleAaret) {
                paragraph {
                    text(
                        bokmal {
                            +"Nedenfor følger en beregning som viser hva du skulle ha fått utbetalt i " +
                                oppgjoersAar.format() + ":"
                        },
                        nynorsk {
                            +"Nedanfor følgjer ei berekning som viser kva du skulle ha fått utbetalt i " +
                                oppgjoersAar.format() + ":"
                        },
                    )
                }
            }
            showIf(erUttakIAaret) {
                paragraph {
                    text(
                        bokmal {
                            +"Nedenfor følger en beregning som viser hva du skulle ha fått utbetalt i " +
                                "perioden " + uttaksdato.format() + " til 31. desember " +
                                oppgjoersAar.format() + ":"
                        },
                        nynorsk {
                            +"Nedanfor følgjer ei berekning som viser kva du skulle ha fått utbetalt i " +
                                "perioden " + uttaksdato.format() + " til 31. desember " +
                                oppgjoersAar.format() + ":"
                        },
                    )
                }
            }
            showIf(erOpphoerIAaret) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Nedenfor følger en beregning som viser hva du skulle ha fått utbetalt i " +
                                    "perioden 1. januar " + oppgjoersAar.format() + " til " +
                                    opphor.format() + ":"
                            },
                            nynorsk {
                                +"Nedanfor følgjer ei berekning som viser kva du skulle ha fått utbetalt i " +
                                    "perioden 1. januar " + oppgjoersAar.format() + " til " +
                                    opphor.format() + ":"
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
                                +"Nedenfor følger en beregning som viser hva du skulle ha fått utbetalt i " +
                                    "perioden " + uttaksdato.format() + " til " + opphor.format() + ":"
                            },
                            nynorsk {
                                +"Nedanfor følgjer ei berekning som viser kva du skulle ha fått utbetalt i " +
                                    "perioden " + uttaksdato.format() + " til " + opphor.format() + ":"
                            },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal { +"Full AFP (uten fradrag for inntekt): " + fullAfp.format(denominator = false) + " kr" },
                    nynorsk { +"Full AFP (utan frådrag for inntekt): " + fullAfp.format(denominator = false) + " kr" },
                )
                newline()
                text(
                    bokmal { +"Inntektsfradraget i AFP for den nye inntekten: " + fradragBeregnetArbeidsInntekt.format(denominator = false) + " kr" },
                    nynorsk { +"Inntektsfrådraget i AFP for den nye inntekta: " + fradragBeregnetArbeidsInntekt.format(denominator = false) + " kr" },
                )
                newline()
                text(
                    bokmal {
                        +inntektIAfpPerioden.format(denominator = false) + " kr (ny beregnet inntekt) / " +
                            tidligereArbeidsInntektBeregnet.format(denominator = false) + " kr (tidligere arbeidsinntekt*) x " +
                            fullAfp.format(denominator = false) + " kr (full AFP)"
                    },
                    nynorsk {
                        +inntektIAfpPerioden.format(denominator = false) + " kr (ny berekna inntekt) / " +
                            tidligereArbeidsInntektBeregnet.format(denominator = false) + " kr (tidlegare arbeidsinntekt*) x " +
                            fullAfp.format(denominator = false) + " kr (full AFP)"
                    },
                )
                newline()
                text(
                    bokmal { +"AFP etter fradrag for den nye inntekten: " + korrigertAfp.format(denominator = false) + " kr" },
                    nynorsk { +"AFP etter frådrag for den nye inntekta: " + korrigertAfp.format(denominator = false) + " kr" },
                )
                newline()
                text(
                    bokmal { +"Tidligere utbetalt AFP: " + utbetaltAfp.format(denominator = false) + " kr" },
                    nynorsk { +"Tidlegare utbetalt AFP: " + utbetaltAfp.format(denominator = false) + " kr" },
                )
                newline()
                text(
                    bokmal { +"*Tidligere arbeidsinntekt er beregnet ut fra inntekten din i årene før du tok ut AFP." },
                    nynorsk { +"*Tidlegare arbeidsinntekt er berekna ut frå inntekta di i åra før du tok ut AFP." },
                )
            }

            paragraph {
                text(
                    bokmal { +"For lite utbetalt AFP: " + forlitebetalt.format(denominator = false) + " kr" },
                    nynorsk { +"For lite utbetalt AFP: " + forlitebetalt.format(denominator = false) + " kr" },
                )
            }
        }
    }

    object RefusjonskravForbehold : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Hvis du har rett til annen samordningspliktig pensjon, må vi ta forbehold om " +
                            "mulige refusjonskrav fra andre pensjonsordninger i etterbetalingen av AFP. Ny " +
                            "samordning mellom to eller flere pensjoner tilbake i tid kan føre til at den " +
                            "aktuelle etterbetalingen av AFP blir vesentlig redusert eller faller helt bort."
                    },
                    nynorsk {
                        +"Dersom du har rett til annan samordningspliktig pensjon, må vi ta atterhald om " +
                            "moglege refusjonskrav frå andre pensjonsordningar i etterbetalinga av AFP. Ny " +
                            "samordning mellom to eller fleire pensjonar tilbake i tid kan føre til at " +
                            "den aktuelle etterbetalinga av AFP blir vesentleg redusert eller fell heilt " +
                            "bort."
                    },
                )
            }
        }
    }

}
