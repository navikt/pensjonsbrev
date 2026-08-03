package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.selectors.barnepensjonInnvilgelseRedigerbartUtfallDTO.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.selectors.barnepensjonInnvilgelseRedigerbartUtfallData.*
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import java.time.LocalDate

data class BarnepensjonInnvilgelseRedigerbartUtfallData(
    val virkningsdato: LocalDate,
    val avdoed: Avdoed?,
    val sisteBeregningsperiodeDatoFom: LocalDate,
    val sisteBeregningsperiodeBeloep: Kroner,
    val erEtterbetaling: Boolean,
    val harFlereUtbetalingsperioder: Boolean,
    val erGjenoppretting: Boolean,
    val harUtbetaling: Boolean,
    val erSluttbehandling: Boolean = false
)

data class BarnepensjonInnvilgelseRedigerbartUtfallDTO(
    override val data: BarnepensjonInnvilgelseRedigerbartUtfallData,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonInnvilgelseRedigerbartUfall : EtterlatteTemplate<BarnepensjonInnvilgelseRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE_UTFALL

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(bokmal { +"" }, nynorsk { +"" }, english { +"" })
        }
        outline {
            includePhrase(
                BarnepensjonInnvilgelseFraser.Foerstegangsbehandlingsvedtak(
                    data.avdoed,
                    data.virkningsdato,
                    data.sisteBeregningsperiodeDatoFom,
                    data.sisteBeregningsperiodeBeloep,
                    data.erEtterbetaling,
                    data.harFlereUtbetalingsperioder,
                    data.erGjenoppretting,
                    data.harUtbetaling,
                    data.erSluttbehandling
                ),
            )
            includePhrase(BarnepensjonInnvilgelseFraser.BegrunnelseForVedtaketRedigerbart(data.erEtterbetaling))
        }
    }
}
