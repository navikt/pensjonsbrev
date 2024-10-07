package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregning
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningsperiodeSelectors.sanksjon
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.datoVedtakOmgjoering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.erOmgjoering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.harFlereUtbetalingsperioder
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.omsRettUtenTidsbegrensning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.tidligereFamiliepleier
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.beregningAvOmstillingsstoenad
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.forhaandsvarselFeilutbetalingOmstillingsstoenadRevurdering
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmOmstillingsstoenad
import java.time.LocalDate

data class OmstillingsstoenadRevurderingDTO(
    override val innhold: List<Element>,
    val innholdForhaandsvarsel: List<Element>,
    val erEndret: Boolean,
    val erOmgjoering: Boolean,
    val datoVedtakOmgjoering: LocalDate?,
    val beregning: OmstillingsstoenadBeregning,
    val etterbetaling: OmstillingsstoenadEtterbetaling?,
    val harFlereUtbetalingsperioder: Boolean,
    val harUtbetaling: Boolean,
    val lavEllerIngenInntekt: Boolean?, // TODO: skal fases ut
    val omsRettUtenTidsbegrensning: Boolean = lavEllerIngenInntekt ?: false, // TODO: overtar for lavEllerIngenInntekt
    val feilutbetaling: FeilutbetalingType,
    val tidligereFamiliepleier: Boolean = false,
) : FerdigstillingBrevDTO

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
                includePhrase(
                    OmstillingsstoenadRevurderingFraser.RevurderingVedtak(
                        erEndret,
                        beregning,
                        etterbetaling.notNull(),
                        harFlereUtbetalingsperioder,
                        harUtbetaling,
                    ),
                )

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
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true),
                beregning,
                tidligereFamiliepleier,
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false),
                beregning,
                tidligereFamiliepleier.not(),
            )

            includeAttachment(informasjonOmOmstillingsstoenad(tidligereFamiliepleier = true), innhold, tidligereFamiliepleier)
            includeAttachment(informasjonOmOmstillingsstoenad(tidligereFamiliepleier = false), innhold, tidligereFamiliepleier.not())

            includeAttachment(dineRettigheterOgPlikter, innhold)
            includeAttachment(
                forhaandsvarselFeilutbetalingOmstillingsstoenadRevurdering,
                this.argument,
                feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL),
            )
        }
}
