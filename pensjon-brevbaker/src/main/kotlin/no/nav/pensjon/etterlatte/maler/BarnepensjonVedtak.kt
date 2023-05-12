package no.nav.pensjon.etterlatte.maler

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.*
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.AvdoedEYBSelectors.doedsdato
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.AvdoedEYBSelectors.navn
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.UtbetalingsinfoSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.UtbetalingsinfoSelectors.beloep
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.UtbetalingsinfoSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.UtbetalingsinfoSelectors.soeskenjustering
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.UtbetalingsinfoSelectors.virkningsdato
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.BarnepensjonVedtakDTOSelectors.utbetalingsinfo
import no.nav.pensjon.etterlatte.maler.fraser.Barnepensjon
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

@TemplateModelHelpers
object BarnepensjonVedtak : EtterlatteTemplate<BarnepensjonVedtakDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK

    override val template = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonVedtakDTO::class,
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

            includePhrase(Barnepensjon.UtbetalingOverskrift)
            includePhrase(
                Barnepensjon.SlikHarViBeregnetPensjonenDin(
                    utbetalingsinfo.beregningsperioder,
                    utbetalingsinfo.soeskenjustering,
                    utbetalingsinfo.antallBarn
                )
            )

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