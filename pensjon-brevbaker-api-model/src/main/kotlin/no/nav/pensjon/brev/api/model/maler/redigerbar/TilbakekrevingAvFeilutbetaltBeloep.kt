package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@Suppress("unused")
data class TilbakekrevingAvFeilutbetaltBeloepDto(
    override val saksbehandlerValg: SaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<TilbakekrevingAvFeilutbetaltBeloepDto.SaksbehandlerValg, TilbakekrevingAvFeilutbetaltBeloepDto.PesysData> {
    data class SaksbehandlerValg(
        val fritekst: Boolean,
    ) : BrevbakerBrevdata

    data class PesysData(
        val sakstype: Sakstype,
        val tilbakekrevingTotal: TilbakekrevingTotalDto,
    ) : BrevbakerBrevdata

    data class TilbakekrevingTotalDto(
        val startPeriodeForTilbakekreving: LocalDate,
        val sluttPeriodeForTilbakekreving: LocalDate,
        val feilutbetaltBeloep: Kroner,
        val ResultatAvVurderingen: ResultatType,
        val sumTilInnkreving: Kroner,
        val hjemmelForTilbakekreving: HjemmelType,
        val bruttoBeloepTilbakekrevd: Kroner,
        val skatteFradragSomInnkreves: Kroner,
        val nettoBeloepUtenRenterTilbakekrevd: Kroner,
        val rentetilleggSomInnkreves: Kroner,
        val tilbakekrevingPerAarListe: tilbakekrevingPerAarListeDto
    ) : BrevbakerBrevdata

    data class TilbakekrevingPerAarListeDto(
        val Aar: String,
        val tilbakekrevingPerManedListe: TilbakekrevingPerManedListeDto
    ) : BrevbakerBrevdata

    data class TilbakekrevingPerManedListeDto(

    )
}