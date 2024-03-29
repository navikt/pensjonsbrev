package no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Avdoed
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTOSelectors.erEtterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTOSelectors.harFlereUtbetalingsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTOSelectors.sisteBeregningsperiodeBeloep
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTOSelectors.sisteBeregningsperiodeDatoFom
import no.nav.pensjon.etterlatte.maler.barnepensjon.innvilgelse.BarnepensjonInnvilgelseRedigerbartUtfallDTOSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import java.time.LocalDate

data class BarnepensjonInnvilgelseRedigerbartUtfallDTO(
    val virkningsdato: LocalDate,
    val avdoed: Avdoed,
    val sisteBeregningsperiodeDatoFom: LocalDate,
    val sisteBeregningsperiodeBeloep: Kroner,
    val erEtterbetaling: Boolean,
    val harFlereUtbetalingsperioder: Boolean,
)

@TemplateModelHelpers
object BarnepensjonInnvilgelseRedigerbartUfall : EtterlatteTemplate<BarnepensjonInnvilgelseRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE_UTFALL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonInnvilgelseRedigerbartUtfallDTO::class,
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
                Language.Bokmal to "Vi innvilger barnepensjonen din",
                Language.Nynorsk to "Vi har innvilga søknaden din om barnepensjon",
                Language.English to "We have granted your application for a children's pension",
            )
        }
        outline {
            includePhrase(Vedtak.BegrunnelseForVedtaket)

            includePhrase(
                BarnepensjonInnvilgelseFraser.Foerstegangsbehandlingsvedtak(
                    avdoed,
                    virkningsdato,
                    sisteBeregningsperiodeDatoFom,
                    sisteBeregningsperiodeBeloep,
                    erEtterbetaling,
                    harFlereUtbetalingsperioder
                ),
            )
        }
    }
}
