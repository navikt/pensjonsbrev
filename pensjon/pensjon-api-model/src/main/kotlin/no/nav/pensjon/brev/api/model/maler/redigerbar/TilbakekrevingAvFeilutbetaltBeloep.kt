package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TilbakekrevingResultat
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.OversiktOverFeilutbetalingPEDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

@Suppress("unused")
// tilbakekrevingTotal data hentes fra v1.TilbakekrevingTotal
data class TilbakekrevingAvFeilutbetaltBeloepDto(
    val feilutbetaltTotalBeloep: Kroner, // feilutbetalingTotal
    val resultatAvVurderingenForTotalBeloep: TilbakekrevingResultat, // resultatTotalType
    val sakstype: Sakstype, // v1.Sak.sakTypeKode
    val sluttPeriodeForTilbakekreving: LocalDate, // periodeTom
    val startPeriodeForTilbakekreving: LocalDate, // periodeFom
    val sumTilInnkrevingTotalBeloep: Kroner, // sumTilInnkreving
    val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
    val oversiktOverFeilutbetalingPEDto: OversiktOverFeilutbetalingPEDto,
) : FagsystemBrevdata

