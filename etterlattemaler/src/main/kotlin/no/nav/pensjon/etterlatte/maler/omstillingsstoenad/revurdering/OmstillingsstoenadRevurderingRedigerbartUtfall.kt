package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRevurderingRedigertbartUtfall
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import java.time.LocalDate
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.harFlereUtbetalingsperioder
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.inntektsAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDataSelectors.mottattInntektendringAutomatisk

data class OmstillingsstoenadRevurderingRedigerbartUtfallData(
    val beregning: OmstillingsstoenadBeregningRevurderingRedigertbartUtfall,
    val erEndret: Boolean,
    val erEtterbetaling: Boolean,
    val etterbetaling: OmstillingsstoenadEtterbetaling?,
    val feilutbetaling: FeilutbetalingType,
    val harFlereUtbetalingsperioder: Boolean,
    val harUtbetaling: Boolean,
    val inntekt: Kroner,
    val inntektsAar: Int,
    val mottattInntektendringAutomatisk: LocalDate?
)

data class OmstillingsstoenadRevurderingRedigerbartUtfallDTO(
    override val data: OmstillingsstoenadRevurderingRedigerbartUtfallData,
): RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadRevurderingRedigerbartUtfall:
    EtterlatteTemplate<OmstillingsstoenadRevurderingRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_REVURDERING_UTFALL

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - revurdering",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }
        outline {
            includePhrase(
                OmstillingsstoenadRevurderingFraser.RevurderingVedtak(
                    data.erEndret,
                    data.beregning,
                    data.etterbetaling.notNull(),
                    data.harFlereUtbetalingsperioder,
                    data.harUtbetaling,
                ),
            )
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(
                OmstillingsstoenadRevurderingFraser.UtfallRedigerbart(
                    data.erEtterbetaling,
                    data.feilutbetaling,
                    data.inntekt,
                    data.inntektsAar,
                    data.mottattInntektendringAutomatisk
                )
            )
            showIf(data.harUtbetaling) {
                includePhrase(OmstillingsstoenadRevurderingFraser.UtbetalingMedEtterbetaling(data.erEtterbetaling))
            }
            showIf(data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingMedVarselRevurdering)
            }
            showIf(data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingUtenVarselRevurdering)
            }
        }
    }
}