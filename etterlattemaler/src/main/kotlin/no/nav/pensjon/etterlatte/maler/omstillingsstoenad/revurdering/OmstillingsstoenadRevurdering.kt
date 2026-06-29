package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.selectors.omstillingsstoenadBeregning.*
import no.nav.pensjon.etterlatte.maler.selectors.omstillingsstoenadBeregningsperiode.*
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.selectors.omstillingsstoenadRevurderingDTO.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.selectors.omstillingsstoenadRevurderingData.*
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.beregningAvOmstillingsstoenad
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.forhaandsvarselFeilutbetalingOmstillingsstoenadRevurdering
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmOmstillingsstoenad
import java.time.LocalDate

data class OmstillingsstoenadRevurderingData(
    val innholdForhaandsvarsel: List<Element>,
    val erEndret: Boolean,
    val erOmgjoering: Boolean,
    val datoVedtakOmgjoering: LocalDate?,
    val beregning: OmstillingsstoenadBeregning,
    val lavEllerIngenInntekt: Boolean?, // TODO: skal fases ut
    val omsRettUtenTidsbegrensning: Boolean = lavEllerIngenInntekt ?: false, // TODO: overtar for lavEllerIngenInntekt
    val feilutbetaling: FeilutbetalingType,
    val tidligereFamiliepleier: Boolean = false,
    val bosattUtland: Boolean = false,
    val erInnvilgelsesaar: Boolean,
) {
    val informasjonOmOmstillingsstoenadData = InformasjonOmOmstillingsstoenadData(tidligereFamiliepleier, bosattUtland)
}

data class OmstillingsstoenadRevurderingDTO(
    override val innhold: List<Element>,
    override val data: OmstillingsstoenadRevurderingData,
): VedleggData, FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadRevurdering: EtterlatteTemplate<OmstillingsstoenadRevurderingDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_REVURDERING

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Vedtak - Revurdering av omstillingsstønad",
                distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
        ) {
            title {
                text(
                    bokmal { +"Vi har " },
                    nynorsk { +"Vi har " },
                    english { +"We have " },
                )
                showIf(data.erOmgjoering) {
                    ifNotNull(data.datoVedtakOmgjoering) {
                        text(
                            bokmal { +"omgjort vedtaket om omstillingsstønad av " + it.format() },
                            nynorsk { +"gjort om vedtaket om omstillingsstønad av " + it.format() },
                            english { +"reversed our decision regarding the adjustment allowance on " + it.format() },
                        )
                    }
                }.orShow {
                    showIf(data.beregning.sisteBeregningsperiode.sanksjon and
                            not(data.beregning.sisteBeregningsperiode.erFakeSanksjon)) {
                        text(
                            bokmal { +"stanset" },
                            nynorsk { +"stansa" },
                            english { +"stopped" },
                        )
                    } orShow {
                        showIf(data.erEndret or data.beregning.sisteBeregningsperiode.erFakeSanksjon) {
                            text(
                                bokmal { +"endret" },
                                nynorsk { +"endra" },
                                english { +"changed" },
                            )
                        } orShow {
                            text(
                                bokmal { +"vurdert" },
                                nynorsk { +"vurdert" },
                                english { +"evaluated" },
                            )
                        }
                    }
                    text(
                        bokmal { +" omstillingsstønaden din" },
                        nynorsk { +" omstillingsstønaden din" },
                        english { +" your adjustment allowance" },
                    )
                }
            }

            outline {
                konverterElementerTilBrevbakerformat(innhold)

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
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true, innvilgelsesaar = true),
                data.beregning,
                data.tidligereFamiliepleier.and(data.erInnvilgelsesaar),
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false, innvilgelsesaar = false),
                data.beregning,
                data.tidligereFamiliepleier.not().and(data.erInnvilgelsesaar.not()),
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true, innvilgelsesaar = false),
                data.beregning,
                data.tidligereFamiliepleier.and(data.erInnvilgelsesaar.not()),
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false, innvilgelsesaar = true),
                data.beregning,
                data.tidligereFamiliepleier.not().and(data.erInnvilgelsesaar),
            )


            includeAttachment(informasjonOmOmstillingsstoenad(), data.informasjonOmOmstillingsstoenadData)

            includeAttachment(dineRettigheterOgPlikter)
            includeAttachment(
                forhaandsvarselFeilutbetalingOmstillingsstoenadRevurdering,
                this.argument,
                data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL),
            )
        }
}
