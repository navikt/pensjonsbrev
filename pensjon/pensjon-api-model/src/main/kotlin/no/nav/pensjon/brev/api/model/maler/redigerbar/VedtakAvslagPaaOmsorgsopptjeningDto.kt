package no.nav.pensjon.brev.api.model.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

@Suppress("unused")
data class VedtakAvslagPaaOmsorgsopptjeningDto(
    val navEnhet: String,
    val omsorgGodskrevetAar: List<Year>,
    val dineRettigheterOgMulighetTilAaKlageDto: DineRettigheterOgMulighetTilAaKlageDto,
) : FagsystemBrevdata