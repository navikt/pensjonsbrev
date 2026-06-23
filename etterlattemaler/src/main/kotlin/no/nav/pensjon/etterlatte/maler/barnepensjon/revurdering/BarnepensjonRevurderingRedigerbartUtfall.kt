package no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.FeilutbetalingType
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.selectors.barnepensjonRevurderingRedigerbartUtfallDTO.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.selectors.barnepensjonRevurderingRedigerbartUtfallData.*
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

data class BarnepensjonRevurderingRedigerbartUtfallData(
    val harUtbetaling: Boolean,
    val feilutbetaling: FeilutbetalingType,
    val brukerUnder18Aar: Boolean,
    val bosattUtland: Boolean,
    val frivilligSkattetrekk: Boolean,
    val erEtterbetaling: Boolean
)

data class BarnepensjonRevurderingRedigerbartUtfallDTO(
    override val data: BarnepensjonRevurderingRedigerbartUtfallData,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonRevurderingRedigerbartUtfall : EtterlatteTemplate<BarnepensjonRevurderingRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_REVURDERING_UTFALL

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
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(BarnepensjonRevurderingFraser.UtfallRedigerbart(data.erEtterbetaling, data.feilutbetaling))
            showIf(data.harUtbetaling) {
                includePhrase(BarnepensjonFellesFraser.UtbetalingAvBarnepensjon(data.erEtterbetaling, data.bosattUtland, data.frivilligSkattetrekk))
            }
            showIf(data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_MED_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingMedVarselRevurdering)
            }
            showIf(data.feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingUtenVarselRevurdering)
            }
        }
    }
}