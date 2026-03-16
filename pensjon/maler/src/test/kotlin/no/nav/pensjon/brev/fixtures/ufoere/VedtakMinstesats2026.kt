package no.nav.pensjon.brev.fixtures.ufoere

import no.nav.pensjon.brev.api.model.maler.ufoerApi.VedtakMinstesats2026Dto
import no.nav.pensjon.brev.fixtures.createOrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.fixtures.createPEgruppe10

fun createVedtakMinstesats2026() =
    VedtakMinstesats2026Dto(
        pe = createPEgruppe10(),
        maanedligUfoeretrygdFoerSkatt = null,
        orienteringOmRettigheterUfoere = createOrienteringOmRettigheterUfoereDto()
    )
