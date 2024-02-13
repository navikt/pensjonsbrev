package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTOSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

data class BarnepensjonRevurderingRedigerbartUtfallDTO(
    val erEtterbetaling: Boolean,
    val harUtbetaling: Boolean,
    val feilutbetaling: FeilutbetalingType
)

@TemplateModelHelpers
object BarnepensjonRevurderingRedigerbartUtfall : EtterlatteTemplate<BarnepensjonRevurderingRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingRedigerbartUtfallDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - revurdering",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi har vurdert barnepensjonen din",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }
        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(BarnepensjonRevurderingFraser.UtfallRedigerbart(erEtterbetaling, feilutbetaling))
            showIf(harUtbetaling) {
                includePhrase(BarnepensjonInnvilgelseFraser.UtbetalingAvBarnepensjon(erEtterbetaling))
            }
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingMedVarselRevurdering)
            }
        }
    }
}