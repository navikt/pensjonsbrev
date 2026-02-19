package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakAvslagPaaOmsorgsopptjeningDto
import no.nav.pensjon.brev.maler.vedlegg.createDineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Year

fun createVedtakAvslagPaaOmsorgsopptjeningDto() =
    VedtakAvslagPaaOmsorgsopptjeningDto(
        saksbehandlerValg = VedtakAvslagPaaOmsorgsopptjeningDto.SaksbehandlerValg(
            omsorgsarbeidFoer1992 = false,
            omsorgsarbeidEtter69Aar = true,
            omsorgsarbeidMindreEnn22TimerOgMindreEnn6Maaneder = false,
            omsorgsarbeidMindreEnn22Timer = false,
            omsorgsarbeidMindreEnn6Maaneder = false,
            privatAFPavslaat = false,
            omsorgsarbeidForBarnUnder7aarFoer1992 = false,
            omsorgsopptjeningenGodskrevetEktefellen = false,
            brukerFoedtFoer1948 = false
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