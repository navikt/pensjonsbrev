package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmEndringDto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.time.Month

fun createVedtakOmEndringDto() = VedtakOmEndringDto(
    saksbehandlerValg = VedtakOmEndringDto.SaksbehandlerValg(
        inntektskontrollDato = LocalDate.of(2021, Month.JANUARY, 1),
        virkFom = LocalDate.of(2022, Month.FEBRUARY, 2),
        alderspensjonPerMaanedFoerSkatt = Kroner(3000),
        skulleVaertRedusertFraDato = LocalDate.of(2023, Month.MARCH, 3)
    ),
    pesysData = VedtakOmEndringDto.PesysData(
        grunnbeloep = Kroner(120_000),
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto()

    )
)