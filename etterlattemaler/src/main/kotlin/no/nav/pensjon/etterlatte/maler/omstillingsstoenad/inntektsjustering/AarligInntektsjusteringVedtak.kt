package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.model.format
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
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser.Utbetaling
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.endringIUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.informasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.inntektsaar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.omsRettUtenTidsbegrensning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.tidligereFamiliepleier
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.beregningAvOmstillingsstoenad
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmOmstillingsstoenad
import java.time.LocalDate

data class AarligInntektsjusteringVedtakDTO(
    override val innhold: ElementListe,
    val beregning: OmstillingsstoenadBeregning,
    val omsRettUtenTidsbegrensning: Boolean = false,
    val tidligereFamiliepleier: Boolean = false,
    val inntektsaar: Int,
    val harUtbetaling: Boolean,
    val endringIUtbetaling: Boolean,
    val virkningstidspunkt: LocalDate,
    val bosattUtland: Boolean,
) : FerdigstillingBrevDTO {
    val informasjonOmOmstillingsstoenadData = InformasjonOmOmstillingsstoenadData(tidligereFamiliepleier, bosattUtland)
}

@TemplateModelHelpers
object OmstillingsstoenadInntektsjusteringVedtak : EtterlatteTemplate<AarligInntektsjusteringVedtakDTO>,
    Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING_VEDTAK

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = AarligInntektsjusteringVedtakDTO::class,
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
                    Bokmal to "Utkast - Vedtak om omstillingsstønad fra 1. januar ".expr() + inntektsaar.format(),
                    Nynorsk to "Utkast - Vedtak om omstillingsstønad frå 1. januar ".expr() + inntektsaar.format(),
                    English to "Draft document - decision regarding adjustment allowance from January 1, ".expr() + inntektsaar.format(),
                )
            }
            outline {
                showIf(endringIUtbetaling){
                    paragraph {
                        textExpr(
                            Bokmal to "Omstillingsstønaden din er endret fra 1. januar ".expr() + inntektsaar.format() + ".",
                            Nynorsk to "Omstillingsstønaden din er endra frå 1. januar  ".expr() + inntektsaar.format() + ".",
                            English to "Your adjustment allowance has changed from January 1, ".expr() + inntektsaar.format() + ".",
                        )
                    }

                    includePhrase(Utbetaling(harUtbetaling, beregning))
                }.orShow {
                    val sisteUtbetalteBeloep = beregning.sisteBeregningsperiode.utbetaltBeloep.format()
                    paragraph {
                        textExpr(
                            Bokmal to "Omstillingsstønaden din er vurdert på nytt fra 1. januar ".expr() + inntektsaar.format() + ". "
                                    + ifElse(harUtbetaling, "Du får fortsatt ".expr()+ sisteUtbetalteBeloep +" kroner per måned før skatt.","Du får fortsatt ikke utbetalt stønad.".expr()),
                            Nynorsk to "Omstillingsstønaden din er vurdert på nytt frå 1. januar  ".expr() + inntektsaar.format() + ". "
                                    + ifElse(harUtbetaling, "Du får framleis ".expr()+ sisteUtbetalteBeloep +" kroner per månad før skatt.", "Du får framleis ikkje utbetalt stønad.".expr()),
                            English to "Your adjustment allowance has been reassessed from January 1, ".expr() + inntektsaar.format() + ". "
                                    + ifElse(harUtbetaling, "You will continue to receive NOK ".expr()+ sisteUtbetalteBeloep +" per month before tax.", "You will still not be paid the allowance.".expr()),
                        )
                    }

                    paragraph {
                        text(
                            Bokmal to "Se hvordan vi har beregnet omstillingsstønaden din i vedlegget «Beregning av " +
                                    "omstillingsstønad».",
                            Nynorsk to "Du kan sjå i vedlegget «Utrekning av omstillingsstønad» korleis vi har " +
                                    "rekna ut omstillingsstønaden din.",
                            English to
                                    "You can see how we calculated your adjustment allowance in the attachment: Calculation of adjustment allowance.",
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

                showIf(omsRettUtenTidsbegrensning.not()) {
                    includePhrase(OmstillingsstoenadRevurderingFraser.Aktivitetsplikt(tidligereFamiliepleier))
                }
                includePhrase(OmstillingsstoenadFellesFraser.MeldFraOmEndringer)
                includePhrase(OmstillingsstoenadFellesFraser.SpesieltOmInntektsendring)
                includePhrase(OmstillingsstoenadFellesFraser.Etteroppgjoer)
                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }

            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true, innvilgelsesaar = false),
                beregning,
                tidligereFamiliepleier,
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false, innvilgelsesaar = false),
                beregning,
                tidligereFamiliepleier.not(),
            )
            includeAttachment(informasjonOmOmstillingsstoenad(), informasjonOmOmstillingsstoenadData)
            includeAttachment(dineRettigheterOgPlikter, beregning)
        }
    }