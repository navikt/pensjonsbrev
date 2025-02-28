package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadBeregningRevurderingRedigertbartUtfall
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadRevurderingFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.erEndret
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.harFlereUtbetalingsperioder
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.inntekt
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.inntektsAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingRedigerbartUtfallDTOSelectors.mottattInntektendringAutomatisk
import java.time.LocalDate

data class OmstillingsstoenadRevurderingRedigerbartUtfallDTO(
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
): RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadRevurderingRedigerbartUtfall:
    EtterlatteTemplate<OmstillingsstoenadRevurderingRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_REVURDERING_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = OmstillingsstoenadRevurderingRedigerbartUtfallDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadataEtterlatte(
            displayTitle = "Vedtak - revurdering",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "",
                Language.Nynorsk to "",
                Language.English to "",
            )
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
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(
                OmstillingsstoenadRevurderingFraser.UtfallRedigerbart(
                    erEtterbetaling,
                    feilutbetaling,
                    inntekt,
                    inntektsAar,
                    mottattInntektendringAutomatisk
                )
            )
            showIf(harUtbetaling) {
                includePhrase(OmstillingsstoenadRevurderingFraser.UtbetalingMedEtterbetaling(erEtterbetaling))
            }
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingMedVarselRevurdering)
            }
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(OmstillingsstoenadRevurderingFraser.FeilutbetalingUtenVarselRevurdering)
            }
        }
    }
}