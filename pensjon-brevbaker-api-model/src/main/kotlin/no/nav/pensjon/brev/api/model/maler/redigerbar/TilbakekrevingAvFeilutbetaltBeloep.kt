package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.KonteringType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
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
        val tilbakekrevingAvFeilUtbetaltBeloep: TilbakekrevingAvFeilutbetaltBeloepDto,
    ) : BrevbakerBrevdata

    data class TilbakekrevingAvFeilUtbetaltBeloepDto(
        val bruttoBeloepTilbakekrevdTotalBeloep: Kroner,
        val feilutbetaltTotalBeloep: Kroner,
        val nettoBeloepUtenRenterTilbakekrevdTotalBeloep: Kroner,
        val rentetilleggSomInnkrevesTotalBeloep: Kroner,
        val resultatAvVurderingenForTotalBeloep: TilbakekrevingResultat,
        val skattefradragSomInnkrevesTotalBeloep: Kroner,
        val sluttPeriodeForTilbakekreving: LocalDate,
        val startPeriodeForTilbakekreving: LocalDate,
        val sumTilInnkrevingTotalBeloep: Kroner,
        val tilbakekrevingPerAar: TilbakekrevingPerAar,
        val tilbakekrevingPerMaaned: TilbakekrevingPerManed,
    ) : BrevbakerBrevdata

    // For hvert år med innslag, skal en liste over tilbakekreving genereres
    data class TilbakekrevingPerAar(
        val Aar: String,
        val tilbakekrevingPerAarListe: List<TilbakekrevingListe>,
    ) : BrevbakerBrevdata

    // For hvert måned med innslag, skal en liste over tilbakekreving generes
    data class TilbakekrevingPerManed(
        val maaned: String,
        val tilbakekrevingPerMaanedListe: List<TilbakekrevingListe>,
        ) : BrevbakerBrevdata

    data class TilbakekrevingListe(
        val bruttoBeloepTilbakekrevd: Kroner,
        val feilutbetaltBeloep: Kroner,
        val nettoBeloepUtenRenterTilbakekrevd: Kroner,
        val resultatAvVurderingen: TilbakekrevingResultat,
        val skattefradragSomInnkreves: Kroner,
        val ytelsenMedFeilutbetaling: KonteringType,
        ) : BrevbakerBrevdata
}