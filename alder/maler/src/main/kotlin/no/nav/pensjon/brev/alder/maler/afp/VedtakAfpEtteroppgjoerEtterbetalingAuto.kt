package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.forlitebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.forventetPensjonsgivendeInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.fradragBeregnetArbeidsInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.fullAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.korrigertAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.tidligereArbeidsInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.toleranseBeloep
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDtoSelectors.utbetaltAfp
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
                                "stor del av inntekten din som er opptjent før og etter at du tok ut AFP."
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

            showIf(medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

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
                            bokmal { +"Feriepenger og lønn som er opptjent før første uttak av AFP" },
                            nynorsk { +"Feriepengar og lønn som er opptente før første uttaket av AFP" },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Honorar, royalty, bonus eller andre inntekter som kommer fra arbeid " +
                                        "eller virksomhet før første uttak av AFP"
                            },
                            nynorsk {
                                +"Honorar, royalty, bonus eller andre inntekter som stammar frå arbeid " +
                                        "eller verksemd før det første uttaket av AFP"
                            },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Etterbetaling av trygdeytelser som gjelder for tidsrom før AFP ble tatt ut"
                            },
                            nynorsk {
                                +"Etterbetaling av trygdeytingar som gjeld for tidsrom før AFP vart tatt ut"
                            },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Inntekt fra arbeid med fordrevne fra Ukraina som skal holdes utenfor etteroppgjøret " },
                    nynorsk { +"" }

                )
                list {
                    item {
                        text(
                            bokmal {
                                +"Feriepenger etter særskilt sats for pensjonistavlønning fra arbeid med fordrevne fra Ukraina. Dette gjelder bare feriepenger fra arbeid til og med 31. desember 2024."
                            },
                            nynorsk {
                                +""
                            },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Vi trenger bekreftelse fra arbeidsgiveren din om: " },
                    nynorsk { +"" }
                )
                list {
                    item {
                        text(
                            bokmal { +"hvor mye du har hatt i feriepenger fra inntekter fra slikt ekstra arbeid" },
                            nynorsk { +"" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"i hvilken tidsperiode(-r) dette gjelder" },
                            nynorsk { +"" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"om utbetalingen er gjort etter særskilt sats for pensjonistavlønning" },
                            nynorsk { +"" }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Reglene om inntekter fra arbeid med fordrevne fra Ukraina finnes i forskrift om kombinasjon av AFP og arbeidsinntekt § 2 tredje ledd." },
                    nynorsk { +"" }
                )
            }

            // Felles paragraf «Annen inntekt som er opptjent samtidig … inntektsprøves».
            // 101-originalen sier "etter uttak av AFP", fellesfrasen sier
            // "etter første uttak av AFP" — kosmetisk, harmoniseres mot fellesfrasen.
            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)

            // Skjema-paragraf — delt med PE_AF_04_100 og PE_AF_04_102.
            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)

            // «Inntekten din i {år}»-tittel + PGI-paragraf.
            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerInnhold.SamletPgiOpplysning(pensjonsgivendeInntekt = pensjonsgivendeInntekt, oppgjoersAar = oppgjoersAar))

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
                    inntektFoerUttak = inntektFoerUttak,
                    inntektEtterOpphoer = inntektEtterOpphoer,
                    inntektIAfpPerioden = inntektIAfpPerioden,
                ),
            )

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din la vi til grunn at du ville ha en forventet " +
                                "arbeidsinntekt på " + forventetPensjonsgivendeInntektBeregnet.format() + ". Etter våre nye beregninger " +
                                "har du hatt en arbeidsinntekt i den perioden du har mottatt AFP som er " +
                                "lavere enn den arbeidsinntekten som ble lagt til grunn ved utbetalingen av " +
                                "pensjon. Denne forskjellen er større enn toleransebeløpet som i " +
                                oppgjoersAar.format() + " var på " + toleranseBeloep.format() + ". Pensjonen din er derfor " +
                                "beregnet på nytt for perioden."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din la vi til grunn at du ville ha ei forventa " +
                                "arbeidsinntekt på " + forventetPensjonsgivendeInntektBeregnet.format() + ". Etter dei nye berekningane " +
                                "våre har du hatt ei arbeidsinntekt i den perioden du har fått AFP, som er " +
                                "lågare enn den arbeidsinntekta som blei lagd til grunn ved utbetalinga av " +
                                "pensjon. Denne forskjellen er større enn toleransebeløpet som i " +
                                oppgjoersAar.format() + " var på " + toleranseBeloep.format() + ". Pensjonen din er derfor " +
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
                    fullAfp = fullAfp,
                    fradragBeregnetArbeidsInntekt = fradragBeregnetArbeidsInntekt,
                    inntektIAfpPerioden = inntektIAfpPerioden,
                    tidligereArbeidsInntektBeregnet = tidligereArbeidsInntektBeregnet,
                    korrigertAfp = korrigertAfp,
                    utbetaltAfp = utbetaltAfp,
                    forlitebetalt = forlitebetalt,
                ),
            )

            // Etterbetalingsfrist + skatt + saksbehandlingstid (kun i fase 1).
            paragraph {
                text(
                    bokmal {
                        +"Du vil få beløpet utbetalt etter at fristen for å sende ny dokumentasjon er " +
                                "gått ut. Det vil bli trukket skatt av etterbetalingsbeløpet. Du må regne med " +
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

            // Avslutning — gjenbrukes fra fellesfrasene.
            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageMedDokumentasjonsfrist)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
