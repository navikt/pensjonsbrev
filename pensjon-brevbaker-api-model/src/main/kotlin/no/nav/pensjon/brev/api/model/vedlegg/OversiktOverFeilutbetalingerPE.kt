package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.KonteringType
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

// tilbakekrevingTotal data hentes fra v1.TilbakekrevingTotal
// Tilbakekreving data hentes fra v1.TilbakekrevingPerManed
data class OversiktOverFeilutbetalingPEDto(
    val bruttoTilbakekrevdTotalbeloep: Kroner,  // bruttoTilbakekrevingTotal
    val nettoUtenRenterTilbakekrevdTotalbeloep: Kroner,  // nettoTilbakrevingTotalUtenRente
    val rentetilleggSomInnkrevesTotalbeloep: Kroner,  // rentetilleggTotal
    val resultatAvVurderingenForTotalbeloep: TilbakekrevingResultat,  // resultatTotal
    val skattefradragSomInnkrevesTotalbeloep: Kroner,  // skattTotal
    val tilbakekrevingPerMaaned: List<Tilbakekreving>,  // tilbakekrevingPerManedListe
) : BrevbakerBrevdata, VedleggBrevdata {
    data class Tilbakekreving(
        val maanedOgAar: LocalDate,  // maned -> feltet returnerer kun måned i dag, utvides med år fra TilbakekrevingTotal
        val bruttobeloepTilbakekrevd: Kroner,  // bruttoTilbakekrevingManed
        val feilutbetaltBeloep: Kroner,  // feilutbetalingManed
        val nettobeloepUtenRenterTilbakekrevd: Kroner,  // nettoTilbakekrevingManedUtenRente
        val resultatAvVurderingen: TilbakekrevingResultat,  // resultatManedType
        val skattefradragSomInnkreves: Kroner,  // skattManed
        val ytelsenMedFeilutbetaling: KonteringType,  // konteringManedType
    )
}

