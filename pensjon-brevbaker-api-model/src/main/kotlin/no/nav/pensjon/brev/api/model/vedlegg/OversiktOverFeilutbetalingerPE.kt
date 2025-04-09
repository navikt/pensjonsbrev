package no.nav.pensjon.brev.api.model.vedlegg

import no.nav.pensjon.brev.api.model.KonteringType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class OversiktOverFeilutbetalingPEDto(
    val bruttoTilbakekrevdTotalbeloep: Kroner,
    val nettoUtenRenterTilbakekrevdTotalbeloep: Kroner,
    val rentetilleggSomInnkrevesTotalbeloep: Kroner?,
    val resultatAvVurderingenForTotalbeloep: TilbakekrevingResultat,
    val skattefradragSomInnkrevesTotalbeloep: Kroner?,
    val tilbakekrevingPerMaaned: List<Tilbakekreving>,
) : BrevbakerBrevdata {
    data class Tilbakekreving(
        val maanedOgAar: LocalDate,
        val bruttobeloepTilbakekrevd: Kroner,
        val feilutbetaltBeloep: Kroner,
        val nettobeloepUtenRenterTilbakekrevd: Kroner,
        val resultatAvVurderingen: TilbakekrevingResultat,
        val skattefradragSomInnkreves: Kroner?,
        val ytelsenMedFeilutbetaling: KonteringType,
    )
}

