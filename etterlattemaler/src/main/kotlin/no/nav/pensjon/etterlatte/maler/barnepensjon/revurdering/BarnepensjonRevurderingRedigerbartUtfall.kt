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
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTOSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTOSelectors.frivilligSkattetrekk
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingRedigerbartUtfallDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

data class BarnepensjonRevurderingRedigerbartUtfallDTO(
    val harUtbetaling: Boolean,
    val feilutbetaling: FeilutbetalingType,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val frivilligSkattetrekk: Boolean,
    val erEtterbetaling: Boolean
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonRevurderingRedigerbartUtfall : EtterlatteTemplate<BarnepensjonRevurderingRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonRevurderingRedigerbartUtfallDTO::class,
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
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(BarnepensjonRevurderingFraser.UtfallRedigerbart(erEtterbetaling, feilutbetaling))
            showIf(harUtbetaling) {
                includePhrase(BarnepensjonFellesFraser.UtbetalingAvBarnepensjon(erEtterbetaling, bosattUtland, frivilligSkattetrekk))
            }
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingMedVarselRevurdering)
            }
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingUtenVarselRevurdering)
            }
        }
    }
}