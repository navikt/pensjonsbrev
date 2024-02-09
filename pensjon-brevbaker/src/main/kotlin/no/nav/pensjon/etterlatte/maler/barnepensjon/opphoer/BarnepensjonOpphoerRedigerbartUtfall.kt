package no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer

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
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerRedigerbartUtfallDTOSelectors.feilutbetaling
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonRevurderingFraser
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.RedigerbartUtfallFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

data class BarnepensjonOpphoerRedigerbartUtfallDTO(
    val feilutbetaling: FeilutbetalingType
)

@TemplateModelHelpers
object BarnepensjonOpphoerRedigerbartUtfall : EtterlatteTemplate<BarnepensjonOpphoerRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_OPPHOER_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonOpphoerRedigerbartUtfallDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi har opphørt barnepensjonen din",
                Language.Nynorsk to "Vi har avvikla barnepensjonen din",
                Language.English to "We have terminated your application for a children's pension",
            )
        }
        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)
            includePhrase(RedigerbartUtfallFraser.FyllInn)
            showIf(feilutbetaling.equalTo(FeilutbetalingType.FEILUTBETALING_UTEN_VARSEL)) {
                includePhrase(BarnepensjonRevurderingFraser.FeilutbetalingUtenVarselOpphoer)
            }
        }
    }
}