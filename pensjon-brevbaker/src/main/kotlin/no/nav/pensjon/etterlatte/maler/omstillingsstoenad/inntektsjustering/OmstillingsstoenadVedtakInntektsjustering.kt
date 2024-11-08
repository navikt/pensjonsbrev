package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.maler.fraser.ufoer.HarDuSpoersmaalEtteroppgjoer
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.datoFOM
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.harEndringIUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.informasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.inntektsaar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.innvilgetMindreEnnFireMndEtterDoedsfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.omsRettUtenTidsbegrensning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.tidligereFamiliepleier
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.beregningAvOmstillingsstoenad
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmOmstillingsstoenad
import java.time.LocalDate

data class OmstillingsstoenadVedtakInntektsjusteringDTO(
    override val innhold: List<Element>,
    val beregning: OmstillingsstoenadBeregning,
    val omsRettUtenTidsbegrensning: Boolean = false,
    val tidligereFamiliepleier: Boolean = false,
    val beregningsperioder: List<OmstillingsstoenadBeregningsperiode>,
    val sisteBeregningsperiode: OmstillingsstoenadBeregningsperiode,
    val inntektsaar: Int,
    val innvilgetMindreEnnFireMndEtterDoedsfall: Boolean = false,
    val harUtbetaling: Boolean,
    val harEndringIUtbetaling: Boolean,
    val virkningstidspunkt: LocalDate,
    val bosattUtland: Boolean,
) : FerdigstillingBrevDTO {
    val informasjonOmOmstillingsstoenadData = InformasjonOmOmstillingsstoenadData(tidligereFamiliepleier, bosattUtland)
}

@TemplateModelHelpers
object OmstillingsstoenadVedtakInntektsjustering : EtterlatteTemplate<OmstillingsstoenadVedtakInntektsjusteringDTO>,
    Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING_VEDTAK

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadVedtakInntektsjusteringDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Vedtaksbrev - inntektsjustering",
                isSensitiv = true,
                distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
        ) {
            title {
                textExpr(
                    Bokmal to "Vedtak om omstillingsstønad fra 1. januar ".expr() + inntektsaar.format(),
                    Nynorsk to "Vedtak om omstillingsstønad frå 1. januar  ".expr() + inntektsaar.format(),
                    English to "Decision regarding adjustment allowance from January 1, ".expr() + inntektsaar.format(),
                )
            }
            outline {
                val harFlerePerioder: Expression<Boolean> = beregningsperioder.size().greaterThan(1)
                val sisteUtbetaltBeloep = sisteBeregningsperiode.utbetaltBeloep
                val datoFomSisteBeregningsperiode = sisteBeregningsperiode.datoFOM

                showIf(harEndringIUtbetaling){
                    paragraph {
                        textExpr(
                            Bokmal to "Omstillingsstønaden din er vurdert på nytt fra 1. januar ".expr() + inntektsaar.format() + "."
                                    + ifElse(harUtbetaling, "Du får fortsatt "+sisteUtbetaltBeloep.format()+" kroner per måned før skatt.","Du får fortsatt ikke utbetalt stønad."),
                            Nynorsk to "Omstillingsstønaden din er vurdert på nytt frå 1. januar  ".expr() + inntektsaar.format() + "."
                                    + ifElse(harUtbetaling, "Du får framleis "+sisteUtbetaltBeloep.format()+" kroner per månad før skatt.", "Du får framleis ikkje utbetalt stønad."),
                            English to "Your adjustment allowance has been reassessed from January 1, ".expr() + inntektsaar.format() + "."
                                    + ifElse(harUtbetaling, "You will continue to receive NOK "+sisteUtbetaltBeloep.format()+" per month before tax.", "You will still not be paid the allowance."),
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget «Beregning av omstillingsstønad».",
                            Nynorsk to "I vedlegget «Utrekning av omstillingsstønad» kan du sjå korleis vi har rekna ut omstillingsstønaden din.",
                            English to "See how we have calculated your adjustment allowance in the attachment «Calculation of adjustment allowance».",
                        )
                    }
                }.orShow {
                    paragraph {
                        textExpr(
                            Bokmal to "Vedtak om omstillingsstønad fra 1. januar ".expr() + inntektsaar.format(),
                            Nynorsk to "Vedtak om omstillingsstønad frå 1. januar  ".expr() + inntektsaar.format(),
                            English to "Decision regarding adjustment allowance from January 1, ".expr() + inntektsaar.format(),
                        )
                    }
                }

                // TODO: denne finnes allerede i OmstillingsstoenadInnvilgelseFraser
                showIf(harUtbetaling) {
                    showIf(harFlerePerioder) {
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ".expr() + sisteUtbetaltBeloep.format() + " kroner hver måned før skatt fra "+ datoFomSisteBeregningsperiode.format() +".",
                                Nynorsk to "Du får ".expr() + sisteUtbetaltBeloep.format() + " kroner kvar månad før skatt frå "+ datoFomSisteBeregningsperiode.format() +".",
                                English to "You will receive  NOK ".expr() + sisteUtbetaltBeloep.format() + " each month before tax from "+ datoFomSisteBeregningsperiode.format() +".",
                            )
                        }
                        paragraph {
                            text(
                                Bokmal to "Se beløp for tidligere perioder og hvordan vi har beregnet omstillingsstønaden i vedlegg «Beregning av omstillingsstønad».",
                                Nynorsk to "I vedlegget «Utrekning av omstillingsstønad» kan du sjå beløp for tidlegare periodar og korleis vi har rekna ut omstillingsstønaden.",
                                English to "See amounts for previous periods and how we have calculated the adjustment allowance in the attachment «Calculation of adjustment allowance».",
                            )
                        }
                    }.orShow {
                        paragraph {
                            textExpr(
                                Bokmal to "Du får ".expr() + sisteUtbetaltBeloep.format() + " kroner hver måned før skatt.",
                                Nynorsk to "Du får ".expr() + sisteUtbetaltBeloep.format() + " kroner kvar månad før skatt.",
                                English to "You will receive  NOK ".expr() + sisteUtbetaltBeloep.format() + " each month before tax.",
                            )
                        }
                        paragraph {
                            text(
                                Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget «Beregning av omstillingsstønad».",
                                Nynorsk to "I vedlegget «Utrekning av omstillingsstønad» kan du sjå korleis vi har rekna ut omstillingsstønaden din.",
                                English to "See how we have calculated your adjustment allowance in the attachment «Calculation of adjustment allowance».",
                            )
                        }
                    }
                }.orShow {
                    paragraph {
                        text(
                            Bokmal to "Du får ikke utbetalt stønad fordi du har inntekt som er høyere enn grensen for å få utbetalt stønaden.",
                            Nynorsk to "Du får ikkje utbetalt stønad, då inntekta di er over grensa for utbetaling av stønad.",
                            English to "You will not receive the allowance, as you have an income that is higher than the threshold for payment of the allowance.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget «Beregning av omstillingsstønad».",
                            Nynorsk to "I vedlegget «Utrekning av omstillingsstønad» kan du sjå korleis vi har rekna ut omstillingsstønaden din.",
                            English to "See how we have calculated your adjustment allowance in the attachment «Calculation of adjustment allowance».",
                        )
                    }
                }

                title2 {
                    text(
                        Bokmal to "Begrunnelse for vedtaket",
                        Nynorsk to "Grunngiving for vedtaket",
                        English to "Grounds for the decision",
                    )
                }

                konverterElementerTilBrevbakerformat(innhold)

                showIf(harUtbetaling) {
                    includePhrase(OmstillingsstoenadFellesFraser.Utbetaling)
                }

                includePhrase(
                    OmstillingsstoenadFellesFraser.HvorLengerKanDuFaaOmstillingsstoenad(
                        beregning,
                        omsRettUtenTidsbegrensning,
                        tidligereFamiliepleier,
                    ),
                )

                showIf(omsRettUtenTidsbegrensning.not()){
                    includePhrase(
                        OmstillingsstoenadInnvilgelseFraser.Aktivitetsplikt(
                            innvilgetMindreEnnFireMndEtterDoedsfall,
                            tidligereFamiliepleier
                        )
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.MeldFraOmEndringer)
                includePhrase(OmstillingsstoenadFellesFraser.SpesieltOmInntektsendring)
                includePhrase(OmstillingsstoenadFellesFraser.Etteroppgjoer)
                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
                includePhrase(HarDuSpoersmaalEtteroppgjoer)
            }

            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true),
                beregning,
                tidligereFamiliepleier,
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false),
                beregning,
                tidligereFamiliepleier.not(),
            )
            includeAttachment(informasjonOmOmstillingsstoenad(), informasjonOmOmstillingsstoenadData)
            includeAttachment(dineRettigheterOgPlikter, beregning)
        }
    }