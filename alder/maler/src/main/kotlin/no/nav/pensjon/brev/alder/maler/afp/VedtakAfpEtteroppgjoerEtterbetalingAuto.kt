package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.felles.Constants.AFP_ETTEROPPGJOER_URL
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

            includePhrase(AfpEtteroppgjoerInnhold.NedenforInntekterBruktOgBeregnet)

            showIf(medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

            // Melding om endringer av inntekten + de to delte innledende paragrafene.
            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

            includePhrase(AfpEtteroppgjoerInnhold.InntektUtenforEtteroppgjoerListe)

            // Felles paragraf «Annen inntekt som er opptjent samtidig … inntektsprøves».
            // 101-originalen sier "etter uttak av AFP", fellesfrasen sier
            // "etter første uttak av AFP" — kosmetisk, harmoniseres mot fellesfrasen.
            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)

            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)

            // Skjema-paragraf — delt med PE_AF_04_100 og PE_AF_04_102.
            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)

            // Covid-19-seksjon: tittel + 2 forklarende paragrafer (felles), så
            // 101-spesifikk bekreftelseliste fra arbeidsgiver.
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmCovidInntekterInnledning)

            // TODO covid-bekreftelseslisten avviker fra PE_AF_04_100 og PE_AF_04_102.
            //  101 har 3 punkter (feriepenger / tidsperiode / særskilt sats), 100 har
            //  3 lignende men med "inntekter" der 101 har "feriepenger", 102 har 4
            //  punkter. Avklar med fag.
            includePhrase(AfpEtteroppgjoerInnhold.CovidDokumentasjonskravFeriepenger)

            // Ukraina-seksjon: tittel + forklarende paragraf (felles), så
            // 101-spesifikk bekreftelseliste.
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmUkrainaUnntak)

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
                            "beregnet på ny for perioden."
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
