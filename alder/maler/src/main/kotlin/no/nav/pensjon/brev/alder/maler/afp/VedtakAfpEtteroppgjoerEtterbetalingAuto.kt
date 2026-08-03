package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.AfpPeriode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.forlitebetalt
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.forventetPensjonsgivendeInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.fradragBeregnetArbeidsInntekt
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.fullAfp
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.korrigertAfp
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.periode
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.tidligereArbeidsInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.toleranseBeloep
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.utbetaltAfp
import no.nav.pensjon.brev.alder.model.afp.selectors.vedtakAfpEtteroppgjoerEtterbetalingAutoDto.uttaksdato
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
            displayTitle = "Vedtak - etterbetaling av for lite utbetalt pensjon - AFP etteroppgjør",
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
            showIf(periode.equalTo(AfpPeriode.HEL_AFP_HELE_AARET)) {
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
            showIf(periode.equalTo(AfpPeriode.UTTAK_I_AARET)) {
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
            showIf(periode.equalTo(AfpPeriode.OPPHOER_I_AARET)) {
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
            showIf(periode.equalTo(AfpPeriode.UTTAK_OG_OPPHOER_I_AARET)) {
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

            includePhrase(AfpEtteroppgjoerInnhold.NedenforInntekterBruktOgBeregnet)

            showIf(medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

            includePhrase(AfpEtteroppgjoerInnhold.InntektUtenforEtteroppgjoerListe)

            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)

            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(oppgjoersAar))

            includePhrase(AfpEtteroppgjoerInnhold.SamletPgiOpplysning(pensjonsgivendeInntekt = pensjonsgivendeInntekt, oppgjoersAar = oppgjoersAar))

            includePhrase(
                AfpEtteroppgjoerInnhold.InntektFoerUttakInntektEtterOpphoerFordelingPerPeriode(
                    erHelAfpHeleAaret = periode.equalTo(AfpPeriode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(AfpPeriode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(AfpPeriode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(AfpPeriode.UTTAK_OG_OPPHOER_I_AARET),
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
                        +"Ved beregningen av pensjonen din la vi til grunn at du ville ha en forventet arbeidsinntekt på " + forventetPensjonsgivendeInntektBeregnet.format() + ". Etter våre nye beregninger har du hatt en arbeidsinntekt i den perioden du har mottatt AFP som er lavere enn den arbeidsinntekten som ble lagt til grunn ved utbetalingen av pensjon. Denne forskjellen er større enn toleransebeløpet som i " + oppgjoersAar.format() + " var på " + toleranseBeloep.format() + ". Pensjonen din er derfor beregnet på nytt for perioden."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din la vi til grunn at du ville ha ei forventa arbeidsinntekt på " + forventetPensjonsgivendeInntektBeregnet.format() + ". Etter dei nye berekningane våre har du hatt ei arbeidsinntekt i den perioden du har fått AFP som er lågare enn den arbeidsinntekta som blei lagd til grunn ved utbetalinga av pensjon. Denne forskjellen er større enn toleransebeløpet som i " + oppgjoersAar.format() + " var på " + toleranseBeloep.format() + ". Pensjonen din er derfor berekna på nytt for perioden."
                    },
                )
            }

            // Hele «Ny pensjonsberegning»-blokken (title1 + intro + tabell +
            // sum). Delt med PE_AF_04_105.
            includePhrase(
                AfpEtteroppgjoerInnhold.NyPensjonsberegningEtterbetalingBlokk(
                    erHeleAaret = periode.equalTo(AfpPeriode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = periode.equalTo(AfpPeriode.UTTAK_I_AARET),
                    erOpphoerIAaret = periode.equalTo(AfpPeriode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = periode.equalTo(AfpPeriode.UTTAK_OG_OPPHOER_I_AARET),
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

            includePhrase(AfpEtteroppgjoerInnhold.RefusjonskravForbehold)

            // Avslutning — gjenbrukes fra fellesfrasene.
            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageMedDokumentasjonsfrist)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
