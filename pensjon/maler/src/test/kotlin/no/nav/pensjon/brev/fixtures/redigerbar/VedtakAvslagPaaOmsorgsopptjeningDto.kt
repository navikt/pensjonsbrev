package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAvslagPaaOmsorgsopptjeningDto() =
    VedtakAvslagPaaOmsorgsopptjeningDto(
        navEnhet = "Nav Enhet",
        omsorgGodskrevetAar = listOf(
            Year(2025),
            Year(2024),
            Year(2023),
        ),
        dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
    )