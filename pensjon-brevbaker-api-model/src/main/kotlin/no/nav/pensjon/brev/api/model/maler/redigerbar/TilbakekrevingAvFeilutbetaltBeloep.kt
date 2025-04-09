package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class TilbakekrevingAvFeilutbetaltBeloepDto(
    override val pesysData: PesysData, override val saksbehandlerValg: EmptyBrevdata
) : RedigerbarBrevdata<EmptyBrevdata, TilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {

    data class PesysData(
        val feilutbetaltTotalBeloep: Kroner,  // v1.TilbakekrevingTotal.feilutbetalingTotal
        val harMotregning: Boolean,  //
        val resultatAvVurderingenForTotalBeloep: TilbakekrevingResultat,  // v1.TilbakekrevingTotal.resultatTotal/resultatTotalType
        val sakstype: Sakstype,  // v1.Sak.sakTypeKode
        val sluttPeriodeForTilbakekreving: LocalDate,  // v1.TilbakekrevingTotal.periodeTom
        val startPeriodeForTilbakekreving: LocalDate,  // v1.TilbakekrevingTotal.periodeFom
        val sumTilInnkrevingTotalBeloep: Kroner,  // v1.TilbakekrevingTotal.sumTilInnkreving
        val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
        val oversiktOverFeilutbetalingPEDto: OversiktOverFeilutbetalingPEDto,
    ) : BrevbakerBrevdata
}

