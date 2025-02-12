package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering

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
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
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
    // TODO: skal fases ut
    val lavEllerIngenInntekt: Boolean?,
    // TODO: overtar for lavEllerIngenInntekt
    val omsRettUtenTidsbegrensning: Boolean = lavEllerIngenInntekt ?: false,
    val feilutbetaling: FeilutbetalingType,
    val tidligereFamiliepleier: Boolean = false,
    val bosattUtland: Boolean = false,
    val erInnvilgelsesaar: Boolean,
) : FerdigstillingBrevDTO {
    val informasjonOmOmstillingsstoenadData = InformasjonOmOmstillingsstoenadData(tidligereFamiliepleier, bosattUtland)
}

@TemplateModelHelpers
object OmstillingsstoenadRevurdering : EtterlatteTemplate<OmstillingsstoenadRevurderingDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_REVURDERING

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadRevurderingDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - Revurdering av omstillingsstønad",
                    isSensitiv = true,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Vi har ",
                    Nynorsk to "Vi har ",
                    English to "We have ",
                )
                showIf(erOmgjoering) {
                    ifNotNull(datoVedtakOmgjoering) {
                        textExpr(
                            Bokmal to "omgjort vedtaket om omstillingsstønad av ".expr() + it.format(),
                            Nynorsk to "gjort om vedtaket om omstillingsstønad av ".expr() + it.format(),
                            English to "reversed our decision regarding the adjustment allowance on ".expr() + it.format(),
                        )
                    }
                }.orShow {
                    showIf(beregning.sisteBeregningsperiode.sanksjon) {
                        text(
                            Bokmal to "stanset",
                            Nynorsk to "stansa",
                            English to "stopped",
                        )
                    } orShow {
                        showIf(erEndret) {
                            text(
                                Bokmal to "endret",
                                Nynorsk to "endra",
                                English to "changed",
                            )
                        } orShow {
                            text(
                                Bokmal to "vurdert",
                                Nynorsk to "vurdert",
                                English to "evaluated",
                            )
                        }
                    }
                    text(
                        Bokmal to " omstillingsstønaden din",
                        Nynorsk to " omstillingsstønaden din",
                        English to " your adjustment allowance",
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
                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
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

            includeAttachment(dineRettigheterOgPlikter, innhold)
            includeAttachment(
                forhaandsvarselFeilutbetalingOmstillingsstoenadRevurdering,
                this.argument,
                feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL),
            )
        }
}
