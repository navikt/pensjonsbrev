package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.KonteringType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate


data class OversiktOverFeilutbetalingPEDto(
    val sakstype: Sakstype,
    val bruttoTilbakekrevdTotalBeloep: Kroner,
    val feilutbetaltTotalBeloep: Kroner,
    val nettoUtenRenterTilbakekrevdTotalBeloep: Kroner,
    val rentetilleggSomInnkrevesTotalBeloep: Kroner,
    val resultatAvVurderingenForTotalBeloep: TilbakekrevingResultat,
    val skattefradragSomInnkrevesTotalBeloep: Kroner,
    val sluttPeriodeForTilbakekreving: LocalDate,
    val startPeriodeForTilbakekreving: LocalDate,
    val sumTilInnkrevingTotalBeloep: Kroner,
    val tilbakekrevingPerAar: TilbakekrevingPerAar,
    val tilbakekrevingPerMaaned: TilbakekrevingPerManed,
) : BrevbakerBrevdata

data class TilbakekrevingPerAar(
    val aar: Year,
    val tilbakekrevingPerMaaned: List<Tilbakekreving>,
)

data class TilbakekrevingPerManed(
    val maaned: Int,
    val tilbakekrevinger: List<Tilbakekreving>,
)

data class Tilbakekreving(
    val bruttoBeloepTilbakekrevd: Kroner,
    val feilutbetaltBeloep: Kroner,
    val nettoBeloepUtenRenterTilbakekrevd: Kroner,
    val resultatAvVurderingen: TilbakekrevingResultat,
    val skattefradragSomInnkreves: Kroner,
    val ytelsenMedFeilutbetaling: KonteringType,
)
