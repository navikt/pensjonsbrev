package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.erFakeSanksjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.datoVedtakOmgjoering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.erInnvilgelsesaar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.erOmgjoering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.informasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.omsRettUtenTidsbegrensning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.tidligereFamiliepleier
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.*
import java.time.LocalDate

data class OmstillingsstoenadRevurderingDTO(
    override val innhold: List<Element>,
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
    val erInnvilgelsesaar: Boolean
): VedleggData, FerdigstillingBrevDTO {
    val informasjonOmOmstillingsstoenadData = InformasjonOmOmstillingsstoenadData(tidligereFamiliepleier, bosattUtland)
}

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
                showIf(erOmgjoering) {
                    ifNotNull(datoVedtakOmgjoering) {
                        text(
                            bokmal { +"omgjort vedtaket om omstillingsstønad av " + it.format() },
                            nynorsk { +"gjort om vedtaket om omstillingsstønad av " + it.format() },
                            english { +"reversed our decision regarding the adjustment allowance on " + it.format() },
                        )
                    }
                }.orShow {
                    showIf(beregning.sisteBeregningsperiode.sanksjon and
                            beregning.sisteBeregningsperiode.erFakeSanksjon.not()) {
                        text(
                            bokmal { +"stanset" },
                            nynorsk { +"stansa" },
                            english { +"stopped" },
                        )
                    } orShow {
                        showIf(erEndret or beregning.sisteBeregningsperiode.erFakeSanksjon) {
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
                includePhrase(Felles.DuHarRettTilAaKlage)
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }

            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true, innvilgelsesaar = true),
                beregning,
                tidligereFamiliepleier.and(erInnvilgelsesaar),
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false, innvilgelsesaar = false),
                beregning,
                tidligereFamiliepleier.not().and(erInnvilgelsesaar.not()),
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true, innvilgelsesaar = false),
                beregning,
                tidligereFamiliepleier.and(erInnvilgelsesaar.not()),
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false, innvilgelsesaar = true),
                beregning,
                tidligereFamiliepleier.not().and(erInnvilgelsesaar),
            )


            includeAttachment(informasjonOmOmstillingsstoenad(), informasjonOmOmstillingsstoenadData)

            includeAttachment(dineRettigheterOgPlikter)
            includeAttachment(
                forhaandsvarselFeilutbetalingOmstillingsstoenadRevurdering,
                this.argument,
                feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL),
            )
        }
}
