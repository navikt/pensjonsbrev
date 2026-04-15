package no.nav.pensjon.brev.alder.maler.etteroppgjoer.afp

import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoer
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerSelectors.afpAvvik
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerSelectors.forventetPensjonsgivendeInntektBeregnet
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpEtteroppgjoerSelectors.iiap
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlag
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlagSelectors.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlagSelectors.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlagSelectors.opphoerForFristen
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlagSelectors.opphoersDato
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlagSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlagSelectors.uttakFoerFristen
import no.nav.pensjon.brev.alder.model.etteroppgjoer.afp.AfpGrunnlagSelectors.uttaksDato
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.safe
import no.nav.pensjon.brev.template.dsl.text

data class AfpEtteroppgjoerVedtakIngenEndringFelles(
    val oppgjoersAar: Expression<Int>,
    val innsenderEnhet: Expression<String>,
    val afpEtteroppgjoer: Expression<AfpEtteroppgjoer>,
    val afpGrunnlag: Expression<AfpGrunnlag>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    val pensjonsgivendeInntekt: Expression<Double> = afpGrunnlag.safe { this.pensjonsgivendeInntekt }.ifNull(0.0)
    val inntektFoerUttak: Expression<Double> = afpGrunnlag.safe { this.inntektFoerUttak }.ifNull(0.0)
    val inntektEtterOpphoer: Expression<Double> = afpGrunnlag.safe { this.inntektEtterOpphoer }.ifNull(0.0)
    val iiap: Expression<Double> = afpEtteroppgjoer.safe { iiap }.ifNull(0.0)
    val forventetPensjonsgivendeInntektBeregnet: Expression<Double> = afpEtteroppgjoer.safe { this.forventetPensjonsgivendeInntektBeregnet }.ifNull(0.0)
    val afpAvvik: Expression<Double> = afpEtteroppgjoer.safe { afpAvvik }.ifNull(0.0)

    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Hvert år foretar " + innsenderEnhet + " en kontroll av AFP når resultatet av skatteligningen foreligger. Hvis du har hatt en annen pensjonsgivende inntekt fra arbeid eller næring enn den forventede inntekten som ble lagt til grunn ved utbetalingen, skal vi foreta en ny beregning av pensjonen på grunnlag av inntektsopplysningene fra Skatteetaten." },
                nynorsk { +"Kvart år gjennomfører " + innsenderEnhet + " ein kontroll av AFP når resultatet av skattelikninga er klart. Dersom du har hatt ei anna pensjonsgivande inntekt frå arbeid eller næring enn den forventa inntekta som blei lagd til grunn ved utbetalinga, skal vi berekne pensjonen på nytt på grunnlag av inntektsopplysningane frå Skatteetaten." },
            )
        }

        paragraph {
            text(
                bokmal { +"Vi har ikke funnet grunnlag for å endre beregningen av pensjonen din i " + oppgjoersAar.format() + " på bakgrunn av inntektsopplysningene fra Skatteetaten." },
                nynorsk { +"Vi har ikkje funne grunnlag for å endre berekninga av pensjonen din i " + oppgjoersAar.format() + " på bakgrunn av inntektsopplysningane frå Skatteetaten." },
            )
        }

        paragraph {
            text(
                bokmal { +"Vi gjør oppmerksom på at innrapporterte inntektsopplysninger fra Skatteetaten ikke skiller mellom hvor stor del av inntekten din som er opptjent før og etter at du tok ut AFP. NAV kan heller ikke se om noen av inntektene stammer fra arbeid i forbindelse med covid-19 eller arbeid med fordrevne fra Ukraina. Noen slike inntekter kan unntas fra inntektsavkortingen." },
                nynorsk { +"Vi gjer merksam på at innrapporterte inntektsopplysningar frå Skatteetaten ikkje skil mellom kor stor del av inntekta di som er opptent før og etter at du tok ut AFP. NAV kan heller ikkje sjå om nokre av inntektene stammar frå arbeid i samband med covid-19 eller arbeid med fordrivne frå Ukraina. Enkelte slike inntekter kan haldast utanfor inntektsavkortingen." },
            )
        }

        paragraph {
            text(
                bokmal { +"Hvis du har hatt lavere pensjonsgivende inntekt den tiden du har hatt AFP enn det våre beregninger viser, kan du ha rett til høyere AFP." },
                nynorsk { +"Dersom du har hatt lågare pensjonsgivande inntekt i perioden med rett til AFP enn det berekningane våre viser, kan du ha rett til høgare AFP." },
            )
        }

        paragraph {
            text(
                bokmal { +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 bokstav d, og tilhørende forskrift om kombinasjon av avtalefestet pensjon for medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt)." },
                nynorsk { +"Vedtaket er gjort etter lov om AFP for medlemmer av Statens pensjonskasse § 3 bokstav 2 og tilhøyrande forskrift om kombinasjon av avtalefestet pensjon for medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt)." },
            )
        }


        title2 {
            text(
                bokmal { +"Melding om endringer av inntekten" },
                nynorsk { +"Melding om endringar av inntekta" },
            )
        }

        paragraph {
            text(
                bokmal { +"Hvis du mener at inntekten som ligger til grunn for beregningen er feil, må du melde fra til oss innen fire uker fra du mottok dette brevet." },
                nynorsk { +"Dersom du meiner at inntekta som ligg til grunn for berekninga, er feil, må du melde frå til oss innan fire veker frå du fekk dette brevet." },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Inntekt fra arbeid eller virksomhet som er opptjent før første uttak av AFP skal holdes utenfor etteroppgjøret. Dette gjelder også hvis inntekten er utbetalt etter at du tok ut AFP. Med første uttak menes første gang du tok ut AFP, uavhengig av om du har tatt ut gradert eller hel pensjon."
                },
                nynorsk {
                    +"Inntekt frå arbeid eller verksemd som er opptent før det første uttaket av AFP, skal haldast utanfor etteroppgjeret. Dette gjeld også dersom inntekta er utbetalt etter at du tok ut AFP. Med første uttak meiner vi første gong du tok ut AFP, uavhengig av om du har teke ut gradert eller heil pensjon."
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Inntekt som skal holdes utenfor etteroppgjøret:"
                },
                nynorsk {
                    +"Inntekt som skal haldast utanfor etteroppgjeret:"
                },
            )
        }

        paragraph {
            list {
                item {
                    text(
                        bokmal {
                            +"Feriepenger og lønn som er opptjent før første uttak av AFP."
                        },
                        nynorsk {
                            +"Feriepengar og lønn som er opptente før første uttaket av AFP."
                        },
                    )
                }
                item {
                    text(
                        bokmal {
                            +"Honorar, royalty, bonus eller andre inntekter som stammer fra arbeid eller virksomhet før første uttak av AFP."
                        },
                        nynorsk {
                            +"Honorar, royalty, bonus eller andre inntekter som stammar frå arbeid eller verksemd før det første uttaket av AFP."
                        },
                    )
                }
                item {
                    text(
                        bokmal {
                            +"Etterbetaling av trygdeytelser som gjelder for tidsrom før AFP ble tatt ut."
                        },
                        nynorsk {
                            +"Etterbetaling av trygdeytingar som gjeld for tidsrom før AFP vart tatt ut."
                        },
                    )
                }
                item {
                    text(
                        bokmal {
                            +"Feriepenger som stammer fra enkelte typer arbeid i forbindelse med covid-19."
                        },
                        nynorsk {
                            +"Feriepengar som skriv seg frå enkelte typar arbeid i samband med covid-19."
                        },
                    )
                }
                item {
                    text(
                        bokmal {
                            +"Inntekt som stammer fra arbeid i forbindelse med fordrevne fra Ukraina."
                        },
                        nynorsk {
                            +"Inntekt som skriv seg frå arbeid i forbindelse med fordrivne frå Ukraina."
                        },
                    )
                }
            }
        }

        paragraph {
            text(
                bokmal {
                    +"Se mer informasjon om arbeid i forbindelse med covid-19 og fordrevne fra Ukraina nedenfor."
                },
                nynorsk {
                    +"Sjå meir informasjon om arbeid i forbindelse med covid-19 og fordrivne frå Ukraina nedanfor."
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Annen inntekt som er opptjent samtidig med utbetaling av AFP, vil bli inntektsprøvd mot pensjonen. Vi gjør for ordens skyld oppmerksom på at feriepenger opptjent etter første uttak av AFP skal inntektsprøves mot pensjonen."
                },
                nynorsk {
                    +"Anna inntekt som er opptent samtidig med utbetaling av AFP, vil bli inntektsprøvd mot pensjonen. Vi gjer for ordens skuld merksam på at feriepengar som er opptente etter uttak av AFP, skal inntektsprøvast mot pensjonen."
                },
            )
        }

        paragraph {
            text(
                bokmal { +"Du må dokumentere hvilke av inntektene dine som skal holdes utenfor avkorting av AFP. Hvis du ikke sender inn ny dokumentasjon innen fristen, benytter vi de opplysningene vi har, og etteroppgjøret vil bli vurdert som avsluttet." },
                nynorsk { +"Du må dokumentere kva delar av inntektene dine som skal haldast utanfor avkorting av AFP. Dersom du ikkje sender inn ny dokumentasjon innan fristen, nyttar vi dei opplysningane vi har, og etteroppgjeret blir rekna som avslutta." },
            )
        }


        paragraph {
            text(
                bokmal { +"På nav.no/afp-etteroppgjør finner du et skjema som du eller arbeidsgiver kan benytte ved innsending av dokumentasjon." },
                nynorsk { +"På nav.no/afp-etteroppgjør finn du eit skjema som du eller arbeidsgivaren kan nytte ved innsending av dokumentasjon." },
            )
        }

        title2 {
            text(
                bokmal { +"Spesielt om inntekter opptjent i forbindelse med covid-19" },
                nynorsk { +" Spesielt om inntekter opptjent i samband med covid-19" },
            )
        }

        paragraph {
            text(
                bokmal { +"Dersom du har påtatt deg ekstra covid-19-relatert arbeid ut over det du vanligvis jobber, kan det hende at inntekten din fra dette arbeidet ikke skal gi avkorting av AFP. Dette gjelder feriepenger av inntekt fra tjeneste fram til 30. juni 2023 som var nødvendig for å ivareta et ekstraordinært personellbehov ved TISK-tiltak (testing, isolering, smittesporing og karantene) eller vaksinering. Det gjelder bare tjeneste med pensjonistlønn etter særskilt sats." },
                nynorsk { +"Dersom du har teke på deg ekstra covid-19-relatert arbeid utover det du vanlegvis jobbar, kan det hende at inntekta di frå dette arbedet ikkje skal gi avkorting av AFP. Dette gjeld feriepenger av inntekt frå teneste fram til 30. juni 2023 som var naudsynt for å ivareta personellbehov ved TISK-tiltak (testing, isolering, smittesporing og karantene) eller vaksinering. Det gjeld berre teneste med pensjonistlønn etter særskilt sats." },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Reglene om dette finnes i forskrift 19. september 2023 nr. 1446 om endring i forskrift om kombinasjon av avtalefestet pensjon for medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivende inntekt) kapittel II nr. 2."
                },
                nynorsk {
                    +"Reglane om dette finst i forskrift 19. september 2023 nr. 1446 om endring i forskrift om kombinasjon av avtalefestet pensjon for medlemmer av Statens pensjonskasse og arbeidsinntekt (pensjonsgivande inntekt) kapittel II nr. 2."
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"For at NAV skal kunne holde slike inntekter utenfor avkorting, må du sende oss dokumentasjon. Se mer på nav.no/afp-etteroppgjør om hvordan du sender dokumentasjon. Vi trenger bekreftelse fra arbeidsgiveren din om følgende:"
                },
                nynorsk {
                    +"For at NAV skal kunne halde slike inntekter utanfor avkorting, må du sende oss dokumentasjon. Sjå meir på nav.no/afp-etteroppgjør. Vi treng stadfesting frå arbeidsgivaren din om følgjande:"
                },
            )
            list {
                item {
                    text(
                        bokmal {
                            +"hvor mye du har hatt i inntekter fra slikt ekstra arbeid"
                        },
                        nynorsk {
                            +"kor mykje du har hatt i feriepengar frå slikt ekstra arbeid"
                        },
                    )
                }
                item {
                    text(
                        bokmal {
                            +"hvilken tidsperiode(-r) feriepengene er opptjent"
                        },
                        nynorsk {
                            +"i kva for tidsperiode (-periodar) feriepengane er tent opp"
                        },
                    )
                }
                item {
                    text(
                        bokmal {
                            +"om utbetalingen er gjort etter særskilt sats for pensjonistavlønning."
                        },
                        nynorsk {
                            +"om utbetalinga er gjort etter særskilt sats for pensjonistavlønning."
                        },
                    )
                }
            }
        }

        title2 {
            text(
                bokmal {
                    +"Spesielt om unntak for pensjonistlønn etter særskilt sats for pensjonister som jobber med fordrevne fra Ukraina"
                },
                nynorsk {
                    +"Spesielt om unntak for pensjonistlønn etter særskilt sats for pensjonistar som har arbeidd med fordrivne frå Ukraina"
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Dersom du har arbeidet med fordrevne fra Ukraina og mottatt pensjonistlønn etter særskilt sats kan inntekten for dette arbeidet unntas fra inntektsavkorting. Det samme gjelder feriepenger for arbeid utført etter 1. april 2022."
                },
                nynorsk {
                    +"Dersom du har arbeidd med fordrivne frå Ukraina og motteke pensjonistlønn etter særskilt sats kan inntekta for dette arbeidet unntakas frå inntektsavkorting. Det same gjeld feriepengar for slikt arbeid utført etter 1. april 2022."
                },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Reglene om inntekter fra arbeid med fordrevne fra Ukraina finnes i forskrift om kombinasjon av AFP og arbeidsinntekt § 2 tredje ledd."
                },
                nynorsk {
                    +"Reglane om inntekter frå arbeid med fordrivne frå Ukraina finst i forskrift om kombinasjon av AFP og arbeidsinntekt § 2 tredje ledd."
                },
            )
        }

        paragraph {
            text(
                bokmal { +"For at Nav skal kunne holde slike inntekter utenfor avkorting, må du sende oss dokumentasjon. Se mer på nav.no/afp-etteroppgjør om hvordan du sender dokumentasjon. Vi trenger bekreftelse fra arbeidsgiveren din om følgende:" },
                nynorsk { +"For at NAv skal kunne halde slike inntekter utanfor avkorting, må du sende oss dokumentasjon.  Sjå meir på nav.no/afp-etteroppgjør.Vi treng stadfesting frå arbeidsgivaren din om følgjande:" },
            )
            list {
                item {
                    text(
                        bokmal { +"hvor mye du har hatt i inntekter fra slikt ekstra arbeid" },
                        nynorsk { +"kor mykje du har hatt i inntekter frå slikt ekstra arbeid" },
                    )
                }
                item {
                    text(
                        bokmal { +"i hvilken tidsperiode(-r) dette gjelder" },
                        nynorsk { +"i kva for tidsperiode (-periodar) feriepengane er tent opp" },
                    )
                }
                item {
                    text(
                        bokmal { +"om utbetalingen er gjort etter særskilt sats for pensjonistavlønning." },
                        nynorsk { +"om utbetalinga er gjort etter særskilt sats for pensjonistavlønning." },
                    )
                }
            }

        }

        title2 {
            text(
                bokmal { +"Inntekten din i " + oppgjoersAar.format() },
                nynorsk { +"Inntekta di i " + oppgjoersAar.format() },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Opplysninger fra Skatteetaten viser at du har hatt en samlet pensjonsgivende inntekt på " + pensjonsgivendeInntekt.format() + " kroner i inntektsåret " + oppgjoersAar.format() + "."
                },
                nynorsk {
                    +"Opplysningar frå Skatteetaten viser at du har hatt ei samla pensjonsgivande inntekt på " + pensjonsgivendeInntekt.format() + " kroner i inntektsåret " + oppgjoersAar.format() + "."
                },
            )
        }

        showIf(afpGrunnlag.uttakFoerFristen and not(afpGrunnlag.opphoerForFristen)) {
            paragraph {
                text(
                    bokmal { +"Vi har lagt til grunn at hele denne inntekten er opptjent samtidig som du har mottatt AFP." },
                    nynorsk { +"Vi har lagt til grunn at heile denne inntekta er opptent samtidig som du har fått AFP." },
                )
            }
        }

        showIf(not(afpGrunnlag.uttakFoerFristen) and not(afpGrunnlag.opphoerForFristen)) {
            paragraph {
                text(
                    bokmal { +"Fordi du har tatt ut AFP fra " + afpGrunnlag.uttaksDato.format() + " benytter vi en standardberegning for å beregne fordelingen av inntekten din før og etter AFP-uttaket. Denne beregningen kan endres dersom du kan dokumentere en annen fordeling av inntekten." },
                    nynorsk { +"Fordi du har teke ut AFP frå " + afpGrunnlag.uttaksDato.format() + ", nyttar vi ei standardberekning for å rekne ut fordelinga av inntekta di før og etter AFP-uttaket. Denne berekninga kan endrast dersom du kan dokumentere ei anna fordeling av inntekta." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi har lagt til grunn at " + inntektFoerUttak.format() + " kroner er opptjent før du tok ut AFP. Dette beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + ". Den delen av inntekten som regnes for å være opptjent i den perioden du har mottatt AFP, er beregnet til " + iiap.format() + " kroner." },
                    nynorsk { +"Vi har lagt til grunn at " + inntektFoerUttak.format() + " kroner er opptente før du tok ut AFP. Dette beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() + ". Den delen av inntekta som blir rekna for å vere opptent i den perioden du har fått AFP, er berekna til " + iiap.format() + " kroner." },
                )
            }
        }

        showIf(afpGrunnlag.uttakFoerFristen and afpGrunnlag.opphoerForFristen) {
            ifNotNull(afpGrunnlag.opphoersDato) { opphoersDato ->
                paragraph {
                    text(
                        bokmal { +"Fordi AFP har opphørt fra " + opphoersDato.format() + " benytter vi en standardberegning for å beregne fordelingen av inntekten din i perioder med og uten AFP. Denne beregningen kan endres dersom du kan dokumentere en annen fordeling av inntekten." },
                        nynorsk { +"Fordi AFP er avslutta frå " + opphoersDato.format() + ", nyttar vi ei standardberekning for å rekne ut fordelinga av inntekta di i periodar med og utan AFP. Denne berekninga kan endrast dersom du kan dokumentere ei anna fordeling av inntekta." },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Vi har lagt til grunn at " + inntektEtterOpphoer.format() + " kroner er opptjent etter at du gikk over fra AFP til annen pensjon, eventuelt etter opphør av AFP. Dette beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() + ".Den delen av inntekten som regnes for å være opptjent i den perioden du har mottatt AFP, er beregnet til " + iiap.format() + " kroner." },
                        nynorsk { +"Vi har lagt til grunn at " + inntektEtterOpphoer.format() + " kroner er opptente etter at du gjekk over frå AFP til annan pensjon, eventuelt etter at AFP er avslutta. Dette beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() + ".Den delen av inntekta som blir rekna for å vere opptent i den perioden du har fått AFP, er berekna til " + iiap.format() + " kroner." },
                    )
                }
            }
        }

        showIf(not(afpGrunnlag.uttakFoerFristen) and afpGrunnlag.opphoerForFristen) {
            paragraph {
                text(
                    bokmal { +"Fordi du ikke har hatt rett til AFP hele året benytter vi en standardberegning for å beregne fordelingen av inntekten din i perioder med og uten AFP i det aktuelle året. Denne beregningen kan endres dersom du kan dokumentere en annen fordeling av inntekten." },
                    nynorsk { +"Fordi du ikkje har hatt rett til AFP heile året, nyttar vi ei standardberekning for å rekne ut fordelinga av inntekta di i periodar med og utan AFP i det aktuelle året. Denne berekninga kan endrast dersom du kan dokumentere ei anna fordeling av inntekta." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi har lagt til grunn at du tjente " + inntektFoerUttak.format() + " kroner før du tok ut AFP og " + inntektEtterOpphoer.format() + " kroner etter at du gikk over fra AFP til annen pensjon, eventuelt etter opphør av AFP. Det samlede beløpet holdes utenfor etteroppgjøret. Den delen av inntekten som regnes for å være opptjent i den perioden du har mottatt AFP, er beregnet til " + iiap.format() + " kroner." },
                    nynorsk { +"Vi har lagt til grunn at du tente " + inntektFoerUttak.format() + " kroner før du tok ut AFP, og " + inntektEtterOpphoer.format() + " kroner etter at du gjekk over frå AFP til annan pensjon, eventuelt etter at AFP tok slutt. Det samla beløpet blir halde utanfor etteroppgjeret. Den delen av inntekta som blir rekna for å vere opptent i den perioden du har fått AFP, er berekna til " + iiap.format() + " kroner." },
                )
            }
        }

        paragraph {
            text(
                bokmal { +"Ved beregningen av pensjonen din for " + oppgjoersAar.format() + " la vi til grunn at du ville ha en forventet arbeidsinntekt på " + forventetPensjonsgivendeInntektBeregnet.format() + " kroner. Forskjellen mellom den tidligere benyttede arbeidsinntekten og den arbeidsinntekten du etter vår beregning har hatt i perioden, utgjør " + afpAvvik.format() + " kroner. Denne forskjellen er ikke større enn toleransebeløpet som i 2024 var på 15 000 kroner." },
                nynorsk { +"Ved berekninga av pensjonen din for " + oppgjoersAar.format() + " la vi til grunn at du ville ha ei forventa arbeidsinntekt på " + forventetPensjonsgivendeInntektBeregnet.format() + " kroner. Forskjellen mellom den tidlegare nytta arbeidsinntekta og den arbeidsinntekta du etter vår berekning har hatt i perioden, utgjer " + afpAvvik.format() + " kroner. Denne forskjellen er ikkje større enn toleransebeløpet som i 2024 var på 15 000 kroner." },
            )
        }

        paragraph {
            text(
                bokmal { +"Pensjonsberegningen for " + oppgjoersAar.format() + " blir derfor ikke endret." },
                nynorsk { +"Pensjonsberekninga for " + oppgjoersAar.format() + " blir derfor ikkje endra." },
            )
        }

        title2 {
            text(
                bokmal { +"Dine plikter" },
                nynorsk { +"Dine plikter" },
            )
        }


        paragraph {
            text(
                bokmal { +"Du har plikt til å melde fra om endringer som har betydning for størrelsen på pensjonen din." },
                nynorsk { +"Du pliktar å melde frå om endringar som har noko å seie for storleiken på pensjonen din." },
            )
        }

        paragraph {
            text(
                bokmal { +"Du må melde fra om endringer i egen inntekt, i familieforhold og ved flytting innen Norge eller til land utenfor Norge. Hvis du mottar grunnpensjon etter sats for enslige, må du melde fra hvis ektefellen, partneren eller samboeren din får en årlig inntekt som er større enn to ganger folketrygdens grunnbeløp eller hvis vedkommende får uføretrygd eller egen pensjon." },
                nynorsk { +"Du må melde frå om endringar i eiga inntekt eller i familieforhold og ved flytting innanfor Noreg eller til land utanfor Noreg. Dersom du får grunnpensjon etter sats for einslege, må du melde frå dersom ektefellen, partnaren eller sambuaren din får ei årleg inntekt som er større enn to gonger grunnbeløpet i folketrygda, eller dersom vedkommande får eigen pensjon." },
            )
        }

        paragraph {
            text(
                bokmal { +"Hvis du får utbetalt for mye pensjon fordi du ikke har meldt fra om endringer, kan vi kreve tilbake det som er for mye utbetalt." },
                nynorsk { +"Dersom du får utbetalt for mykje pensjon fordi du ikkje har meldt frå om endringar, kan vi krevje tilbake det som er for mykje utbetalt." },
            )
        }

        title2 {
            text(
                bokmal { +"Du har rett til å klage" },
                nynorsk { +"Du har rett til å klage" },
            )
        }

        paragraph {
            text(
                bokmal { +"Du kan som nevnt sende inn dokumentasjon på inntekter som du mener skal holdes utenfor etteroppgjøret. NAV vil foreta et nytt etteroppgjør dersom du har sendt ny dokumentasjon for inntekt innen fristen på fire uker. Du vil da motta et nytt vedtak." },
                nynorsk { +"Du kan som nemnt sende inn dokumentasjon på inntekter som du meiner skal haldast utanfor etteroppgjeret. NAV vil gjennomføre eit nytt etteroppgjer dersom du har sendt ny dokumentasjon for inntekt innan fristen på fire veker. Du vil då få eit nytt vedtak." },
            )
        }

        paragraph {
            text(
                bokmal { +"Hvis du mener at det er andre forhold ved vedtaket som ikke er riktig, har du anledning til å klage på vedtaket. Fristen for å klage er seks uker fra du mottar dette brevet." },
                nynorsk { +"Dersom du meiner at det er andre forhold ved vedtaket som ikkje er rette, har du høve til å klage på vedtaket. Fristen for å klage er seks veker frå du får dette brevet." },
            )
        }

        title2 {
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

        title2 {
            text(
                bokmal { +"Har du spørsmål?" },
                nynorsk { +"Har du spørsmål?" },
            )
        }

        paragraph {
            text(
                bokmal { +"Du finner mer informasjon på nav.no/afp-offentlig#etteroppgjor. På nav.no/kontakt kan du chatte eller skrive til oss. Hvis du ikke finner svar på nav.no kan du ringe oss på telefon 55 55 33 34, hverdager 09.00-15.00. Vennlig hilsen" + innsenderEnhet },
                nynorsk { +"Du finn meir informasjon på nav.no/afp-etteroppgjor. På nav.no/kontakt kan du chatte eller skrive til oss. Hvis du ikkje finn svar på nav.no kan du ringe oss på telefon 55 55 33 34, kvardagar 09.00-15.00. Venleg helsing" + innsenderEnhet },
            )
        }
    }
}