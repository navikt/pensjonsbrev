package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingAutoDto.Periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.forlitebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.fpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.fradragberegnetai
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.fullafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.ieo
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.ifu
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.iiap
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.korrigertafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.opphorsdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.periode
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.pgi
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.tpiberegnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.utbetaltafp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.PesysDataSelectors.uttaksdato
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerEtterbetalingDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Redigerbart vedtak — AFP etteroppgjør (SPK) med etterbetaling.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_109`. Auto-varianten av samme situasjon
 * er [VedtakAfpEtteroppgjoerEtterbetalingAuto] (`PE_AF_04_101`).
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerEtterbetaling : RedigerbarTemplate<VedtakAfpEtteroppgjoerEtterbetalingDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_ETTERBETALING

    override val featureToggle = FeatureToggles.vedtakAfpEtteroppgjoerEtterbetaling.toggle

    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør med etterbetaling",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - vedtak i etteroppgjør for " + pesysData.oppgjoersAar.format() },
                nynorsk { +"Avtalefesta pensjon (AFP) - vedtak i etteroppgjer for " + pesysData.oppgjoersAar.format() },
            )
        }

        outline {
            includePhrase(AfpEtteroppgjoerInnhold.EtteroppgjoerIntro)

            showIf(pesysData.periode.equalTo(Periode.HEL_AFP_HELE_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Den nye beregningen av pensjonen for " + pesysData.oppgjoersAar.format() +
                                " viser at du har fått " + pesysData.forlitebetalt.format() +
                                " for lite utbetalt i pensjon."
                        },
                        nynorsk {
                            +"Den nye berekninga av pensjonen for " + pesysData.oppgjoersAar.format() +
                                " viser at du har fått " + pesysData.forlitebetalt.format() +
                                " for lite utbetalt i pensjon."
                        },
                    )
                }
            }
            showIf(pesysData.periode.equalTo(Periode.UTTAK_I_AARET)) {
                paragraph {
                    text(
                        bokmal {
                            +"Den nye beregningen av pensjonen for tidsrommet " + pesysData.uttaksdato.format() +
                                " til 31. desember " + pesysData.oppgjoersAar.format() + " viser at du har fått " +
                                pesysData.forlitebetalt.format() + " for lite utbetalt i pensjon."
                        },
                        nynorsk {
                            +"Den nye berekninga av pensjonen for tidsrommet " + pesysData.uttaksdato.format() +
                                " til 31. desember " + pesysData.oppgjoersAar.format() + " viser at du har fått " +
                                pesysData.forlitebetalt.format() + " for lite utbetalt i pensjon."
                        },
                    )
                }
            }
            showIf(pesysData.periode.equalTo(Periode.OPPHOER_I_AARET)) {
                ifNotNull(pesysData.opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Den nye beregningen av pensjonen for tidsrommet 1. januar " +
                                    pesysData.oppgjoersAar.format() + " til " + opphor.format() +
                                    " viser at du har fått " + pesysData.forlitebetalt.format() +
                                    " for lite utbetalt i pensjon."
                            },
                            nynorsk {
                                +"Den nye berekninga av pensjonen for tidsrommet 1. januar " +
                                    pesysData.oppgjoersAar.format() + " til " + opphor.format() +
                                    " viser at du har fått " + pesysData.forlitebetalt.format() +
                                    " for lite utbetalt i pensjon."
                            },
                        )
                    }
                }
            }
            showIf(pesysData.periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET)) {
                ifNotNull(pesysData.opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Den nye beregningen av pensjonen for tidsrommet " + pesysData.uttaksdato.format() +
                                    " til " + opphor.format() + " viser at du har fått " +
                                    pesysData.forlitebetalt.format() + " for lite utbetalt i pensjon."
                            },
                            nynorsk {
                                +"Den nye berekninga av pensjonen for tidsrommet " + pesysData.uttaksdato.format() +
                                    " til " + opphor.format() + " viser at du har fått " +
                                    pesysData.forlitebetalt.format() + " for lite utbetalt i pensjon."
                            },
                        )
                    }
                }
            }

            includePhrase(AfpEtteroppgjoerInnhold.NedenforInntekterBruktOgBeregnet)

            includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            includePhrase(AfpEtteroppgjoerInnhold.MeldingOmEndringerInnledning)

            includePhrase(AfpEtteroppgjoerInnhold.InntektUtenforEtteroppgjoerListe)

            includePhrase(AfpEtteroppgjoerInnhold.AnnenInntektInntektsproevd)
            includePhrase(AfpEtteroppgjoerInnhold.DokumenterInntekterUtenforAvkorting)
            includePhrase(AfpEtteroppgjoerInnhold.SkjemaForDokumentasjon)
            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmCovidInntekterInnledning)

            includePhrase(AfpEtteroppgjoerInnhold.CovidDokumentasjonskravFeriepenger)

            includePhrase(AfpEtteroppgjoerInnhold.SpesieltOmUkrainaUnntak)
            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(pesysData.oppgjoersAar))
            includePhrase(AfpEtteroppgjoerInnhold.SamletPgiOpplysning(pensjonsgivendeInntekt = pesysData.pgi, oppgjoersAar = pesysData.oppgjoersAar))

            includePhrase(
                AfpEtteroppgjoerInnhold.InntektFoerUttakInntektEtterOpphoerFordelingPerPeriode(
                    erHelAfpHeleAaret = pesysData.periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = pesysData.periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = pesysData.periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = pesysData.periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = pesysData.uttaksdato,
                    opphorsdato = pesysData.opphorsdato,
                    oppgjoersAar = pesysData.oppgjoersAar,
                    inntektFoerUttak = pesysData.ifu,
                    inntektEtterOpphoer = pesysData.ieo,
                    inntektIAfpPerioden = pesysData.iiap,
                ),
            )

            paragraph {
                text(
                    bokmal {
                        +"Ved beregningen av pensjonen din la vi til grunn at du ville ha en forventet " +
                            "arbeidsinntekt på " + pesysData.fpiberegnet.format() + ". Etter våre nye beregninger " +
                            "har du hatt en arbeidsinntekt i den perioden du har mottatt AFP som er " +
                            "lavere enn den arbeidsinntekten som ble lagt til grunn ved utbetalingen av " +
                            "pensjon. Denne forskjellen er større enn toleransebeløpet som i " +
                            pesysData.oppgjoersAar.format() + " var på 15 000 kroner. Pensjonen din er derfor " +
                            "beregnet på ny for perioden."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din la vi til grunn at du ville ha ei forventa " +
                            "arbeidsinntekt på " + pesysData.fpiberegnet.format() + ". Etter dei nye berekningane " +
                            "våre har du hatt ei arbeidsinntekt i den perioden du har fått AFP, som er " +
                            "lågare enn den arbeidsinntekta som blei lagd til grunn ved utbetalinga av " +
                            "pensjon. Denne forskjellen er større enn toleransebeløpet som i " +
                            pesysData.oppgjoersAar.format() + " var på 15 000 kroner. Pensjonen din er derfor " +
                            "berekna på nytt for perioden."
                    },
                )
            }

            includePhrase(
                AfpEtteroppgjoerInnhold.NyPensjonsberegningEtterbetalingBlokk(
                    erHeleAaret = pesysData.periode.equalTo(Periode.HEL_AFP_HELE_AARET),
                    erUttakIAaret = pesysData.periode.equalTo(Periode.UTTAK_I_AARET),
                    erOpphoerIAaret = pesysData.periode.equalTo(Periode.OPPHOER_I_AARET),
                    erUttakOgOpphoerIAaret = pesysData.periode.equalTo(Periode.UTTAK_OG_OPPHOER_I_AARET),
                    uttaksdato = pesysData.uttaksdato,
                    opphorsdato = pesysData.opphorsdato,
                    oppgjoersAar = pesysData.oppgjoersAar,
                    fullAfp = pesysData.fullafp,
                    fradragBeregnetArbeidsInntekt = pesysData.fradragberegnetai,
                    inntektIAfpPerioden = pesysData.iiap,
                    tidligereArbeidsInntektBeregnet = pesysData.tpiberegnet,
                    korrigertAfp = pesysData.korrigertafp,
                    utbetaltAfp = pesysData.utbetaltafp,
                    forlitebetalt = pesysData.forlitebetalt,
                ),
            )

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

            includePhrase(AfpEtteroppgjoerInnhold.RefusjonskravForbehold)

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

            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageMedDokumentasjonsfrist)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
    }
}
