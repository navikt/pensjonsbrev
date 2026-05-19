package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.utbetaltBeloep
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser.Utbetaling
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDataSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDataSelectors.endringIUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDataSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDataSelectors.informasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDataSelectors.inntektsaar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDataSelectors.omsRettUtenTidsbegrensning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDataSelectors.tidligereFamiliepleier
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.beregningAvOmstillingsstoenad
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmOmstillingsstoenad
import java.time.LocalDate

data class AarligInntektsjusteringVedtakData(
    val beregning: OmstillingsstoenadBeregning,
    val omsRettUtenTidsbegrensning: Boolean = false,
    val tidligereFamiliepleier: Boolean = false,
    val inntektsaar: Int,
    val harUtbetaling: Boolean,
    val endringIUtbetaling: Boolean,
    val virkningstidspunkt: LocalDate,
    val bosattUtland: Boolean,
) {
    val informasjonOmOmstillingsstoenadData = InformasjonOmOmstillingsstoenadData(tidligereFamiliepleier, bosattUtland)
}

data class AarligInntektsjusteringVedtakDTO(
    override val innhold: List<Element>,
    override val data: AarligInntektsjusteringVedtakData,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInntektsjusteringVedtak : EtterlatteTemplate<AarligInntektsjusteringVedtakDTO>,
    Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING_VEDTAK

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Vedtaksbrev - inntektsjustering",
                distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
        ) {
            title {
                text(
                    bokmal { +"Utkast - Vedtak om omstillingsstønad fra 1. januar " + data.inntektsaar.format() },
                    nynorsk { +"Utkast - Vedtak om omstillingsstønad frå 1. januar " + data.inntektsaar.format() },
                    english { +"Draft document - decision regarding adjustment allowance from January 1, " + data.inntektsaar.format() },
                )
            }
            outline {
                showIf(data.endringIUtbetaling){
                    paragraph {
                        text(
                            bokmal { +"Omstillingsstønaden din er endret fra 1. januar " + data.inntektsaar.format() + "." },
                            nynorsk { +"Omstillingsstønaden din er endra frå 1. januar  " + data.inntektsaar.format() + "." },
                            english { +"Your adjustment allowance has changed from January 1, " + data.inntektsaar.format() + "." },
                        )
                    }

                    includePhrase(Utbetaling(data.harUtbetaling, data.beregning))
                }.orShow {
                    val sisteUtbetalteBeloep = data.beregning.sisteBeregningsperiode.utbetaltBeloep.format()
                    paragraph {
                        text(
                            bokmal { + "Omstillingsstønaden din er vurdert på nytt fra 1. januar ".expr() + data.inntektsaar.format() + ". "
                                    + ifElse(data.harUtbetaling, "Du får fortsatt ".expr()+ sisteUtbetalteBeloep +" per måned før skatt.","Du får fortsatt ikke utbetalt stønad.".expr()) },
                            nynorsk { +"Omstillingsstønaden din er vurdert på nytt frå 1. januar ".expr() + data.inntektsaar.format() + ". "
                                    + ifElse(data.harUtbetaling, "Du får framleis ".expr()+ sisteUtbetalteBeloep +" per månad før skatt.", "Du får framleis ikkje utbetalt stønad.".expr()) },
                            english { + "Your adjustment allowance has been reassessed from January 1, ".expr() + data.inntektsaar.format() + ". "
                                    + ifElse(data.harUtbetaling, "You will continue to receive ".expr()+ sisteUtbetalteBeloep +" per month before tax.", "You will still not be paid the allowance.".expr())},
                        )
                    }

                    paragraph {
                        text(
                            bokmal { +"Se hvordan vi har beregnet omstillingsstønaden din i vedlegget «Beregning av " +
                                    "omstillingsstønad»." },
                            nynorsk { +"Du kan sjå i vedlegget «Utrekning av omstillingsstønad» korleis vi har " +
                                    "rekna ut omstillingsstønaden din." },
                            english { +
                                    "You can see how we calculated your adjustment allowance in the attachment: Calculation of adjustment allowance." },
                        )
                    }
                }


                title2 {
                    text(
                        bokmal { +"Begrunnelse for vedtaket" },
                        nynorsk { +"Grunngiving for vedtaket" },
                        english { +"Grounds for the decision" },
                    )
                }

                konverterElementerTilBrevbakerformat(innhold)

                showIf(data.harUtbetaling) {
                    includePhrase(OmstillingsstoenadFellesFraser.Utbetaling)
                }

                includePhrase(
                    OmstillingsstoenadFellesFraser.HvorLengerKanDuFaaOmstillingsstoenad(
                        data.beregning,
                        data.omsRettUtenTidsbegrensning,
                        data.tidligereFamiliepleier,
                    ),
                )

                showIf(data.omsRettUtenTidsbegrensning.not()) {
                    includePhrase(OmstillingsstoenadRevurderingFraser.Aktivitetsplikt(data.tidligereFamiliepleier))
                }
                includePhrase(OmstillingsstoenadFellesFraser.MeldFraOmEndringer)
                includePhrase(OmstillingsstoenadFellesFraser.SpesieltOmInntektsendring)
                includePhrase(OmstillingsstoenadFellesFraser.Etteroppgjoer)
                includePhrase(Felles.DuHarRettTilAaKlage)
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }

            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true, innvilgelsesaar = false),
                data.beregning,
                data.tidligereFamiliepleier,
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false, innvilgelsesaar = false),
                data.beregning,
                data.tidligereFamiliepleier.not(),
            )
            includeAttachment(informasjonOmOmstillingsstoenad(), data.informasjonOmOmstillingsstoenadData)
            includeAttachment(dineRettigheterOgPlikter)
        }
    }