package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_OFFENTLIG_URL
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.forlitebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.fpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.fradragberegnetai
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.fullafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.korrigertafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.tpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.utbetaltafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.uttaksdato
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — AFP etteroppgjør med etterbetaling, fase 1 / forhåndsvarsel (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_101`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når avviket
 * mellom forventet og faktisk pensjonsgivende inntekt overstiger
 * toleransebeløpet og resulterer i for lite utbetalt AFP. Bruker får
 * fire uker på å sende inn ny dokumentasjon. Hvis ingen dokumentasjon
 * kommer, gjennomføres etterbetalingen automatisk; ny dokumentasjon
 * fører til fase 2 (PE_AF_04_105 — `…EtterSvarAuto`).
 *
 * Motsatt finansiell retning av [VedtakAfpEtteroppgjoerTilbakekrevingAuto]
 * (PE_AF_04_107) — som er fase 1 for for-mye-betalt.

 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerEtterbetalingAuto : AutobrevTemplate<VedtakAfpEtteroppgjoerEtterbetalingAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_ETTEROPPGJOER_ETTERBETALING_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør med etterbetaling - forhåndsvarsel",
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

            // Periodevariert «for lite utbetalt»-paragraf.
            showIf(periode.equalTo(Periode.HEL_AFP_HELE_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Den nye beregningen av pensjonen for " + oppgjoersAar.format() +
                                " viser at du har fått " + forlitebetalt.format() +
                                " for lite utbetalt i pensjon."
                        },
                        nynorsk {
                            +"Den nye berekninga av pensjonen for " + oppgjoersAar.format() +
                                " viser at du har fått " + forlitebetalt.format() +
                                " for lite utbetalt i pensjon."
                        },
                    )
                }
            }
            showIf(periode.equalTo(Periode.UTTAK_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Den nye beregningen av pensjonen for tidsrommet " + uttaksdato.format() +
                                " til 31. desember " + oppgjoersAar.format() + " viser at du har fått " +
                                forlitebetalt.format() + " for lite utbetalt i pensjon."
                        },
                        nynorsk {
                            +"Den nye berekninga av pensjonen for tidsrommet " + uttaksdato.format() +
                                " til 31. desember " + oppgjoersAar.format() + " viser at du har fått " +
                                forlitebetalt.format() + " for lite utbetalt i pensjon."
                        },
                    )
                }
            }
            showIf(periode.equalTo(Periode.OPPHOER_I_AARET)) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Den nye beregningen av pensjonen for tidsrommet 1. januar " +
                                    oppgjoersAar.format() + " til " + opphor.format() +
                                    " viser at du har fått " + forlitebetalt.format() +
                                    " for lite utbetalt i pensjon."
                            },
                            nynorsk {
                                +"Den nye berekninga av pensjonen for tidsrommet 1. januar " +
                                    oppgjoersAar.format() + " til " + opphor.format() +
                                    " viser at du har fått " + forlitebetalt.format() +
                                    " for lite utbetalt i pensjon."
                            },
                        )
                    }
                }
            }
            showIf(periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET)) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Den nye beregningen av pensjonen for tidsrommet " + uttaksdato.format() +
                                    " til " + opphor.format() + " viser at du har fått " +
                                    forlitebetalt.format() + " for lite utbetalt i pensjon."
                            },
                            nynorsk {
                                +"Den nye berekninga av pensjonen for tidsrommet " + uttaksdato.format() +
                                    " til " + opphor.format() + " viser at du har fått " +
                                    forlitebetalt.format() + " for lite utbetalt i pensjon."
                            },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"Nedenfor kan du se nærmere hvilke inntekter som er brukt og hvordan vi har " +
                            "beregnet din nye pensjon for denne perioden. Vi gjør oppmerksom på at " +
                            "innrapporterte inntektsopplysninger fra Skatteetaten ikke skiller mellom hvor " +
                            "stor del av inntekten din som er opptjent før og etter at du tok ut AFP. Nav " +
                            "kan ikke se om noen av inntektene stammer fra arbeid i forbindelse med " +
                            "covid-19. Vi kan heller ikke se om noen av inntektene er pensjonistlønn etter " +
                            "særskilt sats for arbeid med fordrevne fra Ukraina. Noen slike inntekter kan " +
                            "unntas fra inntektsavkortingen."
                    },
                    nynorsk {
                        +"Nedanfor kan du sjå nærmare kva inntekter som er brukt, og korleis vi har " +
                            "berekna den nye pensjonen din for denne perioden. Vi gjer merksam på at " +
                            "innrapporterte inntektsopplysningar frå Skatteetaten ikkje skil mellom kor " +
                            "stor del av inntekta di som er opptent før og etter at du tok ut AFP. Nav kan " +
                            "heller ikkje sjå om nokre av inntektene stammar frå arbeid i samband med " +
                            "covid-19. Vi kan heller ikkje sjå at noko av inntekta er pensjonistlønn etter " +
                            "særskilt sats for arbeid med fordrivne frå Ukraina. Nokre slike inntekter kan " +
                            "haldast utanfor inntektsavkortinga."
                    },
                )
            }

            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)

            // Melding om endringer av inntekten + de to delte innledende paragrafene.
            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

            // Inlinet: «Inntekt som skal holdes utenfor etteroppgjøret»-liste.
            // TODO ordlydsavvik vs PE_AF_04_100/102 i covid-19-punktet og
            //  Ukraina-punktet. Avklar med fag om listen skal harmoniseres
            //  på tvers av AFP etteroppgjør-brevene.
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
                text(
                    bokmal {
                        +"Se mer informasjon om arbeid i forbindelse med covid-19 og fordrevne fra Ukraina nedenfor."
                    },
                    nynorsk {
                        +"Sjå meir informasjon om arbeid i forbindelse med covid-19 og fordrivne frå Ukraina nedanfor."
                    },
                )
            }

            // Felles paragraf «Annen inntekt som er opptjent samtidig … inntektsprøves».
            // 101-originalen sier "etter uttak av AFP", fellesfrasen sier
            // "etter første uttak av AFP" — kosmetisk, harmoniseres mot fellesfrasen.
            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)

            // Skjema-paragraf — 101 bruker URL `nav.no/afp-offentlig` der
            // fellesfrasen [AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon] bruker
            // `nav.no/afp-etteroppgjør`. Inlinet med kommentar.
            paragraph {
                text(
                    bokmal {
                        +"På $AFP_OFFENTLIG_URL finner du et skjema som du eller arbeidsgiver kan " +
                            "benytte ved innsending av dokumentasjon."
                    },
                    nynorsk {
                        +"På $AFP_OFFENTLIG_URL finn du eit skjema som du eller arbeidsgivaren kan " +
                            "nytte ved innsending av dokumentasjon."
                    },
                )
            }

            // Covid-19-seksjon: tittel + 2 forklarende paragrafer (felles), så
            // 101-spesifikk bekreftelseliste fra arbeidsgiver.
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmCovidInntekterInnledning)

            // TODO covid-bekreftelseslisten avviker fra PE_AF_04_100 og PE_AF_04_102.
            //  101 har 3 punkter (feriepenger / tidsperiode / særskilt sats), 100 har
            //  3 lignende men med "inntekter" der 101 har "feriepenger", 102 har 4
            //  punkter. Avklar med fag.
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
                            bokmal { +"i hvilken tidsperiode(-r) feriepengene er opptjent" },
                            nynorsk { +"i kva for tidsperiode (-periodar) feriepengane er tente opp" },
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

            // Ukraina-seksjon: tittel + forklarende paragraf (felles), så
            // 101-spesifikk bekreftelseliste.
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmUkrainaUnntak)

            // TODO Ukraina-bekreftelseslisten avviker fra PE_AF_04_100/102.
            //  Avklar med fag om listene skal harmoniseres.
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
                            bokmal { +"hvor mye du har hatt i inntekter fra slikt ekstra arbeid" },
                            nynorsk { +"kor mykje du har hatt i inntekter frå slikt ekstra arbeid" },
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

            // «Inntekten din i {år}»-tittel + PGI-paragraf.
            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            paragraph {
                text(
                    bokmal {
                        +"Opplysninger fra Skatteetaten viser at du har hatt en samlet pensjonsgivende " +
                            "inntekt på " + pgi.format() + " i inntektsåret " + oppgjoersAar.format() + "."
                    },
                    nynorsk {
                        +"Opplysningar frå Skattedirektoratet viser at du har hatt ei samla " +
                            "pensjonsgivande inntekt på " + pgi.format() + " i inntektsåret " +
                            oppgjoersAar.format() + "."
                    },
                )
            }

            // Periode-diskriminert fordeling av PGI på periodene med/uten AFP.
            // Delt med PE_AF_04_107 (toleransebeløp). Se phrase for detaljer.
            includePhrase(
                AfpEtteroppgjoerInnhold.IfuIeoFordelingPerPeriode(
                    erHelAfpHeleAaret = periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = uttaksdato,
                    opphorsdato = opphorsdato,
                    oppgjoersAar = oppgjoersAar,
                    ifu = ifu,
                    ieo = ieo,
                    iiap = iiap,
                ),
            )

            // Toleransebeløp-paragrafen — 101 sier "lavere" (etterbetaling).
            // Originalens hardkodede "som i 2024 var på 15 000 kroner" er
            // parametrisert med {oppgjørsår} (samme tilpasning som 105/107).
            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din la vi til grunn at du ville ha en forventet " +
                            "arbeidsinntekt på " + fpiberegnet.format() + ". Etter våre nye beregninger " +
                            "har du hatt en arbeidsinntekt i den perioden du har mottatt AFP som er " +
                            "lavere enn den arbeidsinntekten som ble lagt til grunn ved utbetalingen av " +
                            "pensjon. Denne forskjellen er større enn toleransebeløpet som i " +
                            oppgjoersAar.format() + " var på 15 000 kroner. Pensjonen din er derfor " +
                            "beregnet på ny for perioden."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din la vi til grunn at du ville ha ei forventa " +
                            "arbeidsinntekt på " + fpiberegnet.format() + ". Etter dei nye berekningane " +
                            "våre har du hatt ei arbeidsinntekt i den perioden du har fått AFP, som er " +
                            "lågare enn den arbeidsinntekta som blei lagd til grunn ved utbetalinga av " +
                            "pensjon. Denne forskjellen er større enn toleransebeløpet som i " +
                            oppgjoersAar.format() + " var på 15 000 kroner. Pensjonen din er derfor " +
                            "berekna på nytt for perioden."
                    },
                )
            }

            // Hele «Ny pensjonsberegning»-blokken (title1 + intro + tabell +
            // sum). Delt med PE_AF_04_105.
            includePhrase(
                AfpEtteroppgjoerInnhold.NyPensjonsberegningEtterbetalingBlokk(
                    erHeleAaret = periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = uttaksdato,
                    opphorsdato = opphorsdato,
                    oppgjoersAar = oppgjoersAar,
                    fullafp = fullafp,
                    fradragberegnetai = fradragberegnetai,
                    iiap = iiap,
                    tpiberegnet = tpiberegnet,
                    korrigertafp = korrigertafp,
                    utbetaltafp = utbetaltafp,
                    forlitebetalt = forlitebetalt,
                ),
            )

            // Etterbetalingsfrist + skatt + saksbehandlingstid (kun i fase 1).
            paragraph {
                text(
                    bokmal {
                        +"Du vil få beløpet utbetalt etter at fristen for å sende ny dokumentasjon er " +
                            "gått ut. Det vil bli trukket skatt av etterbetalingsbeløpet. Det må påregnes " +
                            "noe saksbehandlingstid."
                    },
                    nynorsk {
                        +"Du vil få beløpet utbetalt etter at fristen for å sende ny dokumentasjon er " +
                            "gått ut. Det blir trekt skatt av etterbetalingsbeløpet. Du må rekne med noko " +
                            "saksbehandlingstid."
                    },
                )
            }

            // Forbehold om refusjonskrav — delt med PE_AF_04_105.
            includePhrase(AfpEtteroppgjoerInnhold.RefusjonskravForbehold)

            // Ektefelletillegg — 101-spesifikk.
            paragraph {
                text(
                    bokmal {
                        +"I vår pensjonsberegning er det ikke tatt hensyn til eventuelt ektefelletillegg " +
                            "til forsørget ektefelle over 60 år. Dersom du har fått utbetalt " +
                            "ektefelletillegg, kan nye inntektsopplysninger føre til endring av tillegget. " +
                            "Du vil da få eget vedtak om dette."
                    },
                    nynorsk {
                        +"I pensjonsberekninga vår er det ikkje teke omsyn til eventuelt ektefelletillegg " +
                            "til forsørgd ektefelle over 60 år. Dersom du har fått utbetalt " +
                            "ektefelletillegg, kan nye inntektsopplysningar føre til at tillegget blir " +
                            "endra. Du vil då få eige vedtak om dette."
                    },
                )
            }

            // Avslutning — gjenbrukes fra fellesfrasene.
            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageMedDokumentasjonsfrist)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
