package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerAvslutning
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerForklaringer
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpEtteroppgjoerInnhold
import no.nav.pensjon.brev.alder.maler.afp.fraser.AfpTilbakekrevingBody
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggDineRettigheterAfpEo
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto.Scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.avvik
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.formyebetalt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.fradragBeregnetArbeidsInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.fullAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.inntektEtterOpphoer
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.inntektFoerUttak
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.inntektIAfpPerioden
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.korrigertAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.medlemAvApotekerordningen
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.oppgjoersAar
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.scenario
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.tidligereArbeidsInntektBeregnet
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.toleranseBeloep
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.PesysDataSelectors.utbetaltAfp
import no.nav.pensjon.brev.alder.model.afp.VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — AFP etteroppgjør med tilbakekreving, fase 2 (redigerbar).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_104`. Brevet sendes etter et
 * AFP-etteroppgjør (offentlig sektor / Statens pensjonskasse) når bruker
 * har lagt fram nye opplysninger om inntekt, men avviket fortsatt
 * overstiger toleransebeløpet og det blir tilbakekreving av for mye
 * utbetalt AFP. Saksbehandler fyller inn nettobeløp (etter fradrag for
 * skatt) og selve skattefradraget som fritekst i Skribenten.
 *
 * Forklaringen om hvilke nye inntektsopplysninger som er lagt fram har
 * seks gjensidig utelukkende varianter, se
 * [VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto.Scenario].
 */
@TemplateModelHelpers
object VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysninger : RedigerbarTemplate<VedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysningerDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_ETTEROPPGJOER_TILBAKEKREV_NYE_OPPL

    override val featureToggle = FeatureToggles.vedtakAfpEtteroppgjoerTilbakekrevingNyeOpplysninger.toggle

    override val kategori = Brevkategori.ETTEROPPGJOER

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - AFP etteroppgjør med tilbakekreving (nye opplysninger)",
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
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til vårt forhåndsvarsel om etteroppgjør for avtalefestet pensjon (AFP) for " + pesysData.oppgjoersAar.format() + ". Resultatet av etteroppgjøret viser at du har fått for mye utbetalt."
                    },
                    nynorsk {
                        +"Vi viser til førehandsvarselet vårt om etteroppgjer for avtalefesta pensjon (AFP) for " + pesysData.oppgjoersAar.format() + ". Resultatet av etteroppgjeret viser at du har fått for mykje utbetalt."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du må betale tilbake " + fritekst("nettobeløp") + " kroner."
                    },
                    nynorsk {
                        +"Du må betale tilbake " + fritekst("nettobeløp") + " kroner."
                    },
                )
            }

            showIf(pesysData.medlemAvApotekerordningen) {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpApotekerordningen)
            }.orShow {
                includePhrase(AfpEtteroppgjoerInnhold.VedtaksgrunnlagAfpSpk)
            }

            includePhrase(AfpEtteroppgjoerInnhold.InntektenDinIAarTittel(pesysData.oppgjoersAar))

            // Scenariene følger rekkefølgen i kilden.
            showIf(pesysData.scenario.equalTo(Scenario.INGEN_OVERSTYRING_HEL_AFP)) {
                // Unik for 104 — ingen tilsvarende paragraf i 102.
                paragraph {
                    text(
                        bokmal {
                            +"Du har lagt fram nye opplysninger om inntekten din. Dokumentasjonen gir ikke grunnlag for å godkjenne at din pensjonsgivende inntekt på " + pesysData.pensjonsgivendeInntekt.format() + " for " + pesysData.oppgjoersAar.format() + " helt eller delvis kommer fra før uttak av AFP. Den faktiske arbeidsinntekten din i den perioden du har mottatt AFP er satt til " + pesysData.inntektIAfpPerioden.format() + "."
                        },
                        nynorsk {
                            +"Du har lagt fram nye opplysningar om inntekta di. Dokumentasjonen gir ikkje grunnlag for å godkjenne at den pensjonsgivande inntekta di på " + pesysData.pensjonsgivendeInntekt.format() + " for " + pesysData.oppgjoersAar.format() + " heilt eller delvis kjem frå før du tok ut AFP. Den faktiske arbeidsinntekta di i den perioden du har fått AFP er sett til " + pesysData.inntektIAfpPerioden.format() + "."
                        },
                    )
                }
            }.orShowIf(pesysData.scenario.equalTo(Scenario.INGEN_OVERSTYRING_UTTAK_I_AARET)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.IngenNyeOpplysningerOmEndretInntektFoerUttak(
                        inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(
                        inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak,
                    )
                )
            }.orShowIf(pesysData.scenario.equalTo(Scenario.IFU_OVERSTYRT_UTTAK_I_AARET)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakIAaret(
                        inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(
                        inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak,
                    )
                )
            }.orShowIf(pesysData.scenario.equalTo(Scenario.IFU_OVERSTYRT_HEL_AFP)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.IfuOverstyrtUttakFoerAaret(
                        inntektFoerUttak = pesysData.inntektFoerUttak, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIfu(
                        inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak,
                    )
                )
            }.orShowIf(pesysData.scenario.equalTo(Scenario.IFU_OG_IEO_OVERSTYRT)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.IfuOgIeoOverstyrt(
                        inntektFoerUttak = pesysData.inntektFoerUttak, inntektEtterOpphoer = pesysData.inntektEtterOpphoer, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenIfuOgIeo(
                        inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektFoerUttak = pesysData.inntektFoerUttak, inntektEtterOpphoer = pesysData.inntektEtterOpphoer,
                    )
                )
            }.orShowIf(pesysData.scenario.equalTo(Scenario.KUN_IEO_OVERSTYRT)) {
                includePhrase(
                    AfpEtteroppgjoerForklaringer.KunIeoOverstyrt(
                        inntektEtterOpphoer = pesysData.inntektEtterOpphoer, oppgjoersAar = pesysData.oppgjoersAar,
                    )
                )
                includePhrase(
                    AfpEtteroppgjoerForklaringer.DenFaktiskeArbeidsinntektenKunIeo(
                        inntektIAfpPerioden = pesysData.inntektIAfpPerioden, oppgjoersAar = pesysData.oppgjoersAar, pensjonsgivendeInntekt = pesysData.pensjonsgivendeInntekt, inntektEtterOpphoer = pesysData.inntektEtterOpphoer,
                    )
                )
            }

            // Tilbakekrevings-likninger og forklaringer — identisk med 107.
            includePhrase(
                AfpTilbakekrevingBody.ToleransebeloepOverskrider(
                    avvik = pesysData.avvik, oppgjoersAar = pesysData.oppgjoersAar, toleranseBeloep = pesysData.toleranseBeloep,
                )
            )
            includePhrase(
                AfpTilbakekrevingBody.NyPensjonsberegningEquation(
                    fullAfp = pesysData.fullAfp, fradragBeregnetArbeidsInntekt = pesysData.fradragBeregnetArbeidsInntekt, korrigertAfp = pesysData.korrigertAfp
                )
            )
            includePhrase(
                AfpTilbakekrevingBody.InntektsfradragetFormel(
                    fradragBeregnetArbeidsInntekt = pesysData.fradragBeregnetArbeidsInntekt, inntektIAfpPerioden = pesysData.inntektIAfpPerioden, tidligereArbeidsInntektBeregnet = pesysData.tidligereArbeidsInntektBeregnet, fullAfp = pesysData.fullAfp
                )
            )
            includePhrase(
                AfpTilbakekrevingBody.AfpForMyeEquation(
                    utbetaltAfp = pesysData.utbetaltAfp, korrigertAfp = pesysData.korrigertAfp, formyebetalt = pesysData.formyebetalt
                )
            )

            title1 {
                text(
                    bokmal { +"Beløpet du skal betale tilbake etter fradrag for innbetalt skatt" },
                    nynorsk { +"Beløpet du skal betale tilbake etter fradrag for innbetalt skatt" },
                )
            }

            paragraph {
                table(
                    header = {
                        column(columnSpan = 1) {
                            text(
                                bokmal {
                                    +"Beregning"
                                },
                                nynorsk {
                                    +"Berekning"
                                },
                            )
                        }
                        column(columnSpan = 1) {
                            text(
                                bokmal { +"" },
                                nynorsk { +"" },
                            )
                        }
                    }) {
                    row {
                        cell {
                            text(
                                bokmal { +"For mye utbetalt AFP" },
                                nynorsk { +"For mykje utbetalt AFP" },
                            )
                        }
                        cell {
                            includePhrase(KronerText(pesysData.formyebetalt))
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"− Fradrag for innbetalt skatt" },
                                nynorsk { +"− Frådrag for innbetalt skatt" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("fradrag") + " kr" },
                                nynorsk { +fritekst("fradrag") + " kr" },
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"= Beløpet du skal betale tilbake" },
                                nynorsk { +"= Beløpet du skal betale tilbake" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("nettobeløp") + " kr" },
                                nynorsk { +fritekst("nettobeløp") + " kr" },
                            )
                        }
                    }
                }
            }

            includePhrase(AfpTilbakekrevingBody.SkatteoppgjorParagraph(pesysData.oppgjoersAar))
            includePhrase(AfpTilbakekrevingBody.TilbakebetalingSection)

            includePhrase(AfpEtteroppgjoerAvslutning.DinePlikter)
            includePhrase(AfpEtteroppgjoerAvslutning.DuHarRettTilAaKlageSeksUker)
            paragraph {
                text(
                    bokmal { +"I vedlegget får du vite mer om hvordan du går fram." },
                    nynorsk { +"I vedlegget får du vite meir om korleis du går fram." },
                )
            }
            includePhrase(HarDuSpoersmaal.afpEtteroppgjoer)
        }
        includeAttachment(vedleggDineRettigheterAfpEo)
    }
}
