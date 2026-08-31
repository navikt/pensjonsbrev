package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year

fun createVedtakAvslagPaaOmsorgsopptjeningDto() =
    VedtakAvslagPaaOmsorgsopptjeningDto(
        saksbehandlerValg = lagSaksbehandlervalg(
            "omsorgsarbeidFoer1992" to false,
            "omsorgsarbeidEtter69Aar" to true,
            "omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder" to false,
            "omsorgsarbeidMindreEnn22Timer" to false,
            "omsorgsarbeidMindreEnn6Maaneder" to false,
            "privatAFPavslaat" to false,
            "omsorgsarbeidForBarnUnder7aarFoer1992" to false,
            "omsorgsopptjeningenGodskrevetEktefellen" to false,
            "brukerFoedtFoer1948" to false,
        ),
        pesysData = VedtakAvslagPaaOmsorgsopptjeningDto.PesysData(
            navEnhet = "Nav Enhet",
            omsorgGodskrevetAar = listOf(
                Year(2025),
                Year(2024),
                Year(2023),
            ),
            dineRettigheterOgMulighetTilAaKlageDto = createDineRettigheterOgMulighetTilAaKlageDto()
        )
    )