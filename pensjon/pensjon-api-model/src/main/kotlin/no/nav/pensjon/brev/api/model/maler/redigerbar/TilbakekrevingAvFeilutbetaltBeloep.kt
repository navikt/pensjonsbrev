package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.BrevdataMedSaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

@Suppress("unused")
data class TilbakekrevingAvFeilutbetaltBeloepDto(
    override val pesysData: PesysData, override val saksbehandlerValg: SaksbehandlervalgIDSL,
) : BrevdataMedSaksbehandlerValg<TilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {
// tilbakekrevingTotal data hentes fra v1.TilbakekrevingTotal
    data class PesysData(
        val feilutbetaltTotalBeloep: Kroner, // feilutbetalingTotal
        val resultatAvVurderingenForTotalBeloep: TilbakekrevingResultat, // resultatTotalType
        val sakstype: Sakstype, // v1.Sak.sakTypeKode
        val sluttPeriodeForTilbakekreving: LocalDate, // periodeTom
        val startPeriodeForTilbakekreving: LocalDate, // periodeFom
        val sumTilInnkrevingTotalBeloep: Kroner, // sumTilInnkreving
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
        val oversiktOverFeilutbetalingPEDto: OversiktOverFeilutbetalingPEDto,
    ) : FagsystemBrevdata
}

