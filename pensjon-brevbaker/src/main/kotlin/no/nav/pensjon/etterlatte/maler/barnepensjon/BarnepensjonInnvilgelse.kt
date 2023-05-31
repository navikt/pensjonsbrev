package no.nav.pensjon.etterlatte.maler.barnepensjon

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.*
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTO
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.AvdoedEYBSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.AvdoedEYBSelectors.navn
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.beloep
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.soeskenjustering
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.UtbetalingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.BarnepensjonInnvilgelseDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

@TemplateModelHelpers
object BarnepensjonInnvilgelse : EtterlatteTemplate<BarnepensjonInnvilgelseDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNVILGELSE

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonInnvilgelseDTO::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd fordi du fyller 20 år",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Language.Bokmal to "Vi har innvilget søknaden din om barnepensjon",
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)
            includePhrase(
                Barnepensjon.Vedtak(
                    utbetalingsinfo.virkningsdato,
                    avdoed.navn,
                    avdoed.doedsdato,
                    utbetalingsinfo.beloep
                )
            )

            includePhrase(Barnepensjon.BeregningOgUtbetalingOverskrift)
            includePhrase(
                Barnepensjon.SlikHarViBeregnetPensjonenDin(
                    utbetalingsinfo.beregningsperioder,
                    utbetalingsinfo.soeskenjustering,
                    utbetalingsinfo.antallBarn
                )
            )
            includePhrase(Barnepensjon.Utbetaling)
            includePhrase(Barnepensjon.Regulering)

            includePhrase(Barnepensjon.InformasjonTilDegOverskrift)
            includePhrase(Barnepensjon.MeldFraOmEndringer)
            includePhrase(Barnepensjon.EndringAvKontonummer)
            includePhrase(Barnepensjon.SkattetrekkPaaBarnepensjon)
            includePhrase(Barnepensjon.DuHarRettTilAaKlage)
            includePhrase(Barnepensjon.DuHarRettTilInnsyn)
            includePhrase(Barnepensjon.HarDuSpoersmaal)
        }

    }
}