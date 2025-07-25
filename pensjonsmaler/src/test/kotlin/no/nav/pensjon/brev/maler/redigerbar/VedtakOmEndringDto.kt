package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakOmEndringDto
import no.nav.pensjon.brev.fixtures.createMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.createOrienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brevbaker.api.model.Kroner

fun createVedtakOmEndringDto() = VedtakOmEndringDto(
    saksbehandlerValg = VedtakOmEndringDto.SaksbehandlerValg(
        relasjon = VedtakOmEndringDto.Relasjon.SAMBOER
    ),
    pesysData = VedtakOmEndringDto.PesysData(
        grunnbeloep = Kroner(120_000),
        maanedligPensjonFoerSkattDto = createMaanedligPensjonFoerSkatt(),
        orienteringOmRettigheterOgPlikterDto = createOrienteringOmRettigheterOgPlikterDto()

    )
)