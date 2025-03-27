package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class TilbakekrevingAvFeilutbetaltBeloepDto(
    override val pesysData: PesysData,
    override val saksbehandlerValg: SaksbehandlerValg,
) : RedigerbarBrevdata<TilbakekrevingAvFeilutbetaltBeloepDto.SaksbehandlerValg, TilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {
    data class SaksbehandlerValg(
        val harMotregning: Boolean
    ) : BrevbakerBrevdata

    data class PesysData(
        val feilutbetaltTotalBeloep: Kroner,
        val resultatAvVurderingenForTotalBeloep: TilbakekrevingResultat,
        val sakstype: Sakstype,
        val sluttPeriodeForTilbakekreving: LocalDate,
        val startPeriodeForTilbakekreving: LocalDate,
        val sumTilInnkrevingTotalBeloep: Kroner,
    ) : BrevbakerBrevdata
}