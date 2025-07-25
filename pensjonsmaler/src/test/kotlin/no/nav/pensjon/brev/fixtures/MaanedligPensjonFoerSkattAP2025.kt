package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

fun createMaanedligPensjonFoerSkattAP2025() = MaanedligPensjonFoerSkattAP2025Dto(
    beregnetPensjonPerManedGjeldende = MaanedligPensjonFoerSkattAP2025Dto.AlderspensjonPerManed(
        inntektspensjon = Kroner(1000),
        totalPensjon = Kroner(2000),
        garantipensjon = Kroner(1000),
        minstenivaIndividuell = Kroner(0),
        virkDatoFom = LocalDate.now(),
        virkDatoTom = null
    ),
    beregnetPensjonperManed = listOf(),
    kravVirkFom = LocalDate.now()
)