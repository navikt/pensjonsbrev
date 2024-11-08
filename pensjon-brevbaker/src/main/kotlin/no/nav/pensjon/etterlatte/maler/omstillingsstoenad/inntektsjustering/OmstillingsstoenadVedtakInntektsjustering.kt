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
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser.Utbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.endringIUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.informasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.inntektsaar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.innvilgetMindreEnnFireMndEtterDoedsfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadVedtakInntektsjusteringDTOSelectors.omsRettUtenTidsbegrensning
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
    val inntektsaar: Int,
    val innvilgetMindreEnnFireMndEtterDoedsfall: Boolean = false,
    val harUtbetaling: Boolean,
    val endringIUtbetaling: Boolean,
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

                showIf(endringIUtbetaling){
                    val sisteUtbetaltBeloep = beregning.sisteBeregningsperiode.utbetaltBeloep

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

                includePhrase(Utbetaling(harUtbetaling, beregning))

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