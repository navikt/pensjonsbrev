package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.fpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerToleransebeloepAutoDtoSelectors.uttaksdato
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — ingen endring (innenfor toleransebeløpet) — AFP etteroppgjør (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_100`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når forskjellen
 * mellom forventet og faktisk pensjonsgivende inntekt ikke overstiger
 * toleransebeløpet (15 000 kroner i 2024), og pensjonsberegningen derfor ikke
 * skal endres.
 *
 * Brevet har fire periodevarianter for inntektsfordelingen (uttak/opphør av
 * AFP i løpet av oppgjørsåret), modellert som
 * [VedtakAfpEtteroppgjoerToleransebeloepAutoDto.Periode]. Mye av innholdet
 * (intro, hjemmel, lister over inntekter som holdes utenfor, covid-19/Ukraina
 * og avslutningen) deles med `VedtakAfpEtteroppgjoerIngenEndringAuto`
 * (PE_AF_04_102) — se [AfpEtteroppgjoerInnhold] og [AfpEtteroppgjoerAvslutning].
 *
 * Konverterte avvik fra kilden:
 *  - De fire `showIf`-blokkene for periodevarianter ble i originalen uttrykt
 *    som overlappende rådata-booleans. Logikken er løftet ut av malen til en
 *    [Periode]-diskriminator, jf. skill-step 7.
 *  - «kontaktinformasjon.navnavsenderenhet» er erstattet med "Nav" (brevbaker
 *    setter avsenderenhet via fellesAuto-mekanismen).
 *  - To `showIf`-blokker i originalen for samme periodescenario er slått
 *    sammen.
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerToleransebeloepAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerToleransebeloepAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_TOLERANSEBELOP_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ingen endring (innenfor toleransebeløp) - AFP etteroppgjør",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)
            includePhrase(AfpEtteroppgjoerInnhold.IkkeFunnetGrunnlagForAaEndre(oppgjoersAar))

            paragraph {
                text(
                    bokmal {
                        +"Vi gjør oppmerksom på at innrapporterte inntektsopplysninger fra Skatteetaten " +
                            "ikke skiller mellom hvor stor del av inntekten din som er opptjent før og " +
                            "etter at du tok ut AFP. Nav kan heller ikke se om noen av inntektene stammer " +
                            "fra arbeid i forbindelse med covid-19 eller arbeid med fordrevne fra Ukraina. " +
                            "Noen slike inntekter kan unntas fra inntektsavkortingen."
                    },
                    nynorsk {
                        +"Vi gjer merksam på at innrapporterte inntektsopplysningar frå Skatteetaten ikkje " +
                            "skil mellom kor stor del av inntekta di som er opptent før og etter at du tok " +
                            "ut AFP. Nav kan heller ikkje sjå om nokre av inntektene stammar frå arbeid i " +
                            "samband med covid-19 eller arbeid med fordrivne frå Ukraina. Enkelte slike " +
                            "inntekter kan haldast utanfor inntektsavkortingen."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Hvis du har hatt lavere pensjonsgivende inntekt den tiden du har hatt AFP enn " +
                            "det våre beregninger viser, kan du ha rett til høyere AFP."
                    },
                    nynorsk {
                        +"Dersom du har hatt lågare pensjonsgivande inntekt i perioden med rett til AFP " +
                            "enn det berekningane våre viser, kan du ha rett til høgare AFP."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)

            // Melding om endringer av inntekten + dokumentasjon + covid-19 + Ukraina (alltid).
            // Identisk innhold med PE_AF_04_102 er trukket ut til delte fraser; bare paragrafer
            // med ordlydsforskjeller er inlinet og markert med TODO for faglig gjennomgang.
            //
            // Seksjon 1: Melding om endringer av inntekten.
            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

            // Inlinet: «Inntekt som skal holdes utenfor etteroppgjøret» — covid-19-punktet
            // i listen avviker fra PE_AF_04_102.
            paragraph {
                text(
                    bokmal { +"Inntekt som skal holdes utenfor etteroppgjøret:" },
                    nynorsk { +"Inntekt som skal haldast utanfor etteroppgjeret:" },
                )
                list {
                    item {
                        text(
                            bokmal { +"Feriepenger og lønn som er opptjent før første uttak av AFP." },
                            nynorsk { +"Feriepengar og lønn som er opptente før første uttaket av AFP." },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Honorar, royalty, bonus eller andre inntekter som stammer fra arbeid " +
                                    "eller virksomhet før første uttak av AFP."
                            },
                            nynorsk {
                                +"Honorar, royalty, bonus eller andre inntekter som stammar frå arbeid " +
                                    "eller verksemd før det første uttaket av AFP."
                            },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Etterbetaling av trygdeytelser som gjelder for tidsrom før AFP ble " +
                                    "tatt ut."
                            },
                            nynorsk {
                                +"Etterbetaling av trygdeytingar som gjeld for tidsrom før AFP vart " +
                                    "tatt ut."
                            },
                        )
                    }
                    // TODO ordlydsavvik vs PE_AF_04_102: 04_102 har "Feriepenger som stammer fra arbeid med covid-19." / "...covid-19-pandemien.". Avklar med fag om ordlyden skal harmoniseres.
                    item {
                        text(
                            bokmal {
                                +"Feriepenger som stammer fra enkelte typer arbeid i forbindelse med " +
                                    "covid-19."
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
                text(
                    bokmal {
                        +"Se mer informasjon om arbeid i forbindelse med covid-19 og fordrevne fra " +
                            "Ukraina nedenfor."
                    },
                    nynorsk {
                        +"Sjå meir informasjon om arbeid i forbindelse med covid-19 og fordrivne frå " +
                            "Ukraina nedanfor."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

            // Inlinet: «Du må dokumentere ...» — bokmål avviker fra PE_AF_04_102.
            // TODO bokmål-ordlyd avviker fra PE_AF_04_102: 04_102 har "Du må dokumentere at inntekten er opptjent eller stammer fra arbeid eller virksomhet før uttaket av AFP. ... benytter vi de foreliggende opplysningene ...". Avklar med fag.
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

            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)

            // Seksjon 2: Spesielt om inntekter opptjent i forbindelse med covid-19.
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmCovidInntekterInnledning)

            // Inlinet: covid-19-dokumentasjonslisten — avviker fra PE_AF_04_102.
            // TODO dokumentasjonslisten avviker fra PE_AF_04_102: 04_102 har 4 punkter (med "om arbeidet var beordret eller frivillig") og bokmål-ordlyd "i hvilken tidsperiode(-r) dette gjelder". Avklar med fag om listen skal harmoniseres.
            paragraph {
                text(
                    bokmal {
                        +"For at Nav skal kunne holde slike inntekter utenfor avkorting, må du sende " +
                            "oss dokumentasjon. Se mer på $AFP_ETTEROPPGJOER_URL om hvordan du sender " +
                            "dokumentasjon. Vi trenger bekreftelse fra arbeidsgiveren din om følgende:"
                    },
                    nynorsk {
                        +"For at Nav skal kunne halde slike inntekter utanfor avkorting, må du sende " +
                            "oss dokumentasjon. Sjå meir på $AFP_ETTEROPPGJOER_URL. Vi treng stadfesting " +
                            "frå arbeidsgivaren din om følgjande:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal { +"hvor mye du har hatt i inntekter fra slikt ekstra arbeid" },
                            nynorsk { +"kor mykje du har hatt i feriepengar frå slikt ekstra arbeid" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"hvilken tidsperiode(-r) feriepengene er opptjent" },
                            nynorsk { +"i kva for tidsperiode (-periodar) feriepengane er tent opp" },
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

            // Seksjon 3: Spesielt om unntak ... fordrevne fra Ukraina.
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmUkrainaUnntak)

            // Inlinet: Ukraina-dokumentasjonslisten — avviker fra PE_AF_04_102.
            // TODO dokumentasjonslisten avviker fra PE_AF_04_102: 04_102 bokmål bruker "feriepenger" (ikke "inntekter") i første punkt, og 04_102 nynorsk har "dette gjeld" i tidsperiode-punktet. Avklar med fag.
            paragraph {
                text(
                    bokmal {
                        +"For at Nav skal kunne holde slike inntekter utenfor avkorting, må du sende oss " +
                            "dokumentasjon. Se mer på $AFP_ETTEROPPGJOER_URL om hvordan du sender " +
                            "dokumentasjon. Vi trenger bekreftelse fra arbeidsgiveren din om følgende:"
                    },
                    nynorsk {
                        +"For at Nav skal kunne halde slike inntekter utanfor avkorting, må du sende oss " +
                            "dokumentasjon. Sjå meir på $AFP_ETTEROPPGJOER_URL. Vi treng stadfesting frå " +
                            "arbeidsgivaren din om følgjande:"
                    },
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
                            bokmal { +"om utbetalingen er gjort etter særskilt sats for pensjonistavlønning" },
                            nynorsk { +"om utbetalinga er gjort etter særskilt sats for pensjonistavlønning" },
                        )
                    }
                }
            }

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            paragraph {
                text(
                    bokmal {
                        +"Opplysninger fra Skatteetaten viser at du har hatt en samlet pensjonsgivende " +
                            "inntekt på " + pgi.format() + " i inntektsåret " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Opplysningar frå Skatteetaten viser at du har hatt ei samla pensjonsgivande " +
                            "inntekt på " + pgi.format() + " i inntektsåret " + oppgjoersAar.format() + "."
                    },
                )
            }

            // Hadde AFP hele året — hele inntekten regnes som opptjent samtidig med AFP.
            showIf(periode.equalTo(Periode.HEL_AFP_HELE_AARET)) {
                paragraph {
                    text(
                        bokmal { +"Vi har lagt til grunn at hele denne inntekten er opptjent samtidig som du har mottatt AFP." },
                        nynorsk { +"Vi har lagt til grunn at heile denne inntekta er opptent samtidig som du har fått AFP." },
                    )
                }
            }

            // AFP startet i året — fordeling før/etter uttak.
            showIf(periode.equalTo(Periode.UTTAK_I_AARET)) {
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
                            +"Vi har lagt til grunn at " + ifu.format() + " er opptjent før du tok ut AFP. " +
                                "Dette beløpet skal holdes utenfor etteroppgjøret for " +
                                oppgjoersAar.format() + ". Den delen av inntekten som regnes for å være " +
                                "opptjent i den perioden du har mottatt AFP, er beregnet til " +
                                iiap.format() + "."
                        },
                        nynorsk {
                            +"Vi har lagt til grunn at " + ifu.format() + " er opptente før du tok ut AFP. " +
                                "Dette beløpet skal haldast utanfor etteroppgjeret for " +
                                oppgjoersAar.format() + ". Den delen av inntekta som blir rekna for å vere " +
                                "opptent i den perioden du har fått AFP, er berekna til " +
                                iiap.format() + "."
                        },
                    )
                }
            }

            // AFP opphørte i året — fordeling under/etter AFP.
            showIf(periode.equalTo(Periode.OPPHOER_I_AARET)) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Fordi AFP har opphørt fra " + opphor.format() + " benytter vi " +
                                    "en standardberegning for å beregne fordelingen av inntekten din i " +
                                    "perioder med og uten AFP. Denne beregningen kan endres dersom du kan " +
                                    "dokumentere en annen fordeling av inntekten."
                            },
                            nynorsk {
                                +"Fordi AFP er avslutta frå " + opphor.format() + ", nyttar vi " +
                                    "ei standardberekning for å rekne ut fordelinga av inntekta di i periodar " +
                                    "med og utan AFP. Denne berekninga kan endrast dersom du kan dokumentere " +
                                    "ei anna fordeling av inntekta."
                            },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal {
                            +"Vi har lagt til grunn at " + ieo.format() + " er opptjent etter at du gikk " +
                                "over fra AFP til annen pensjon, eventuelt etter opphør av AFP. Dette " +
                                "beløpet skal holdes utenfor etteroppgjøret for " + oppgjoersAar.format() +
                                ". Den delen av inntekten som regnes for å være opptjent i den perioden du " +
                                "har mottatt AFP, er beregnet til " + iiap.format() + "."
                        },
                        nynorsk {
                            +"Vi har lagt til grunn at " + ieo.format() + " er opptente etter at du gjekk " +
                                "over frå AFP til annan pensjon, eventuelt etter at AFP er avslutta. Dette " +
                                "beløpet skal haldast utanfor etteroppgjeret for " + oppgjoersAar.format() +
                                ". Den delen av inntekta som blir rekna for å vere opptent i den perioden " +
                                "du har fått AFP, er berekna til " + iiap.format() + "."
                        },
                    )
                }
            }

            // Både uttak og opphør i samme år.
            showIf(periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET)) {
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
                            +"Vi har lagt til grunn at du tjente " + ifu.format() + " før du tok ut AFP og " +
                                ieo.format() + " etter at du gikk over fra AFP til annen pensjon, " +
                                "eventuelt etter opphør av AFP. Det samlede beløpet holdes utenfor " +
                                "etteroppgjøret. Den delen av inntekten som regnes for å være opptjent i " +
                                "den perioden du har mottatt AFP, er beregnet til " + iiap.format() + "."
                        },
                        nynorsk {
                            +"Vi har lagt til grunn at du tente " + ifu.format() + " før du tok ut AFP, og " +
                                ieo.format() + " etter at du gjekk over frå AFP til annan pensjon, " +
                                "eventuelt etter at AFP tok slutt. Det samla beløpet blir halde utanfor " +
                                "etteroppgjeret. Den delen av inntekta som blir rekna for å vere opptent i " +
                                "den perioden du har fått AFP, er berekna til " + iiap.format() + "."
                        },
                    )
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din for " + oppgjoersAar.format() + " la vi til grunn " +
                            "at du ville ha en forventet arbeidsinntekt på " + fpiberegnet.format() + ". " +
                            "Forskjellen mellom den tidligere benyttede arbeidsinntekten og den " +
                            "arbeidsinntekten du etter vår beregning har hatt i perioden, utgjør " +
                            avvik.format() + ". Denne forskjellen er ikke større enn toleransebeløpet som " +
                            "i 2024 var på 15 000 kroner."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din for " + oppgjoersAar.format() + " la vi til grunn " +
                            "at du ville ha ei forventa arbeidsinntekt på " + fpiberegnet.format() + ". " +
                            "Forskjellen mellom den tidlegare nytta arbeidsinntekta og den arbeidsinntekta " +
                            "du etter vår berekning har hatt i perioden, utgjer " + avvik.format() + ". " +
                            "Denne forskjellen er ikkje større enn toleransebeløpet som i 2024 var på " +
                            "15 000 kroner."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.PensjonsberegningenBlirIkkeEndret(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)

            title1 {
                text(
                    bokmal { +"Du har rett til å klage" },
                    nynorsk { +"Du har rett til å klage" },
                )
            }
            // TODO: Klage-seksjonen avviker fra PE_AF_04_102 / [AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageSeksUker]
            //  — 04_100 har en ekstra «Du kan som nevnt sende inn dokumentasjon»-paragraf, og selve
            //  klage-paragrafen har annen ordlyd (referer ikke til ${'$'}KLAGE_URL). Avklar med fag om de
            //  to brevene skal harmoniseres.
            paragraph {
                text(
                    bokmal {
                        +"Du kan som nevnt sende inn dokumentasjon på inntekter som du mener skal holdes " +
                            "utenfor etteroppgjøret. Nav vil foreta et nytt etteroppgjør dersom du har sendt " +
                            "ny dokumentasjon for inntekt innen fristen på fire uker. Du vil da motta et " +
                            "nytt vedtak."
                    },
                    nynorsk {
                        +"Du kan som nemnt sende inn dokumentasjon på inntekter som du meiner skal haldast " +
                            "utanfor etteroppgjeret. Nav vil gjennomføre eit nytt etteroppgjer dersom du har " +
                            "sendt ny dokumentasjon for inntekt innan fristen på fire veker. Du vil då få " +
                            "eit nytt vedtak."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at det er andre forhold ved vedtaket som ikke er riktig, har du " +
                            "anledning til å klage på vedtaket. Fristen for å klage er seks uker fra du " +
                            "mottar dette brevet."
                    },
                    nynorsk {
                        +"Dersom du meiner at det er andre forhold ved vedtaket som ikkje er rette, har du " +
                            "høve til å klage på vedtaket. Fristen for å klage er seks veker frå du får " +
                            "dette brevet."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(AfpEtteroppgjoerAvslutning.HarDuSporsmal)
        }
    }
}
